<template>
  <div class="main-content">
    <div style="background-color: white;padding: 15px 20px;height: 45px">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>日程仓库</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="width: 80%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <div style="font-size: 30px; font-weight: bold; text-align: center; width: 100%; line-height: 50px; height: 50px; border-bottom: #cccccc 2px solid">
        日程仓库
      </div>
      <div style="height: 80px; display: flex; padding: 20px 10px; border-bottom: #cccccc 2px solid;margin-bottom: 5px">
        <div class="search" style="margin-top: 4px">
          <el-input placeholder="请输入事件名称查询" style="width: 200px" v-model="eventName"></el-input>
          <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
          <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
          <el-button type="primary" plain style="margin-left: 10px" @click="addVisible=true">新建日程事件</el-button>
        </div>
      </div>
      <el-dialog title="信息" :visible.sync="addVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" :model="addEvent" ref="formRef">
          <el-form-item prop="name" label="事件名称">
            <el-input v-model="addEvent.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="categoryId" label="事件类别">
            <el-select v-model="addEvent.categoryId" placeholder="请选择分类">
              <el-option v-for="item in categoryData" :label="item.name" :value="item.id" :key="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="start" label="开始时间">
            <el-time-picker
                v-model="addEvent.start"
                type="time"
                placeholder="选择时间">
            </el-time-picker>
          </el-form-item>
          <el-form-item prop="last" label="持续时间">
            <el-time-picker
                v-model="displayLast"
                @change="handleChange"
                type="time"
                placeholder="选择时间">
            </el-time-picker>
          </el-form-item>
          <el-form-item prop="address" label="事件地点">
            <el-input v-model="addEvent.address" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="description" label="事件描述">
            <el-input v-model="addEvent.description" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="事件图标">
            <el-upload
                class="avatar-uploader"
                :action="$baseUrl + '/files/upload'"
                :headers="{ token: user.token }"
                list-type="picture"
                :on-success="handleImageSuccess"
            >
              <el-button type="primary">上传图标</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveAdd(addEvent)">确 定</el-button>
        </div>
      </el-dialog>
      <div>
      <el-row style="padding: 5px 10px" :gutter="20">
        <el-col v-for="(item, index) in eventData" :span="5" style="width: 25%">
          <el-card style="width: 100%; padding: 5px 2.5px; margin-top:10px">
            <div style="height: 215px; border-radius: 10px;width: 100%;object-fit: contain;line-height: 180px;text-align: center; margin: 0 auto" v-if="item.status === 'STATIC'">
              <a href="#" @click="navTo('/front/eventDetail?id=' + item.id)"><img :src="item.img" class="image" style="border-radius: 10px;width: 100%; height: 100%; margin: 0 auto;object-fit: contain"></a>
            </div>
            <div style="height: 215px; border-radius: 10px;width: 100%;object-fit: contain;line-height: 180px;text-align: center; margin: 0 auto" v-else>
              <img :src="item.img" class="image" style="border-radius: 10px;width: 100%; height: 100%; margin: 0 auto;object-fit: contain">
            </div>
            <div style="padding: 14px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
              <span style="font-weight: bold" @click="navTo('/front/eventDetail?id=' + item.id)">{{ item.name }}</span>
              <div class="bottom clearfix">
                <time class="time" style="; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{ item.categoryName }}</time>
              </div>
              <div @click="HandleAddToSchedule(index)" style="margin-top: 5px">
                <el-button type="text" class="button">添加至今日计划表</el-button>
              </div>
              <div @click="HandleDeleteFromRepository(index)" style="margin-top: 0">
                <el-button type="text" class="button" style="color: red">移除日程事件</el-button>
              </div>
              <el-dialog title="删除日程" :visible.sync="item.deleteConfirmVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                  当前事件 {{item.name}} 将从日程仓库移除，该操作不可逆，是否继续？
                </div>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="hideDeleteBar(index)" style="height: 50%">取消</el-button>
                  <el-button type="primary" @click="HandleDelete(item)" style="height: 50%">确认</el-button>
                </div>
              </el-dialog>
              <el-dialog title="支付金额" :visible.sync="item.purchaseVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; padding: 0px 20px">
                  支付金额：{{item.price}}<br>
                  当前余额：{{user.money}}<br>
                  是否继续？
                </div>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="hidePurchaseBar(index)" style="height: 50%">取消</el-button>
                  <el-button type="primary" @click="purchase(item)" style="height: 50%">确认</el-button>
                </div>
              </el-dialog>
              <el-dialog title="修改并添加至日程表" :visible.sync="item.modifyVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                <el-form label-width="100px" style="padding-right: 50px" :model="item" ref="formRef">
                  <el-form-item prop="name" label="事件名称">
                    <el-input v-model="item.name" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item prop="start" label="开始时间">
                    <el-time-picker
                        v-model="item.start"
                        type="time"
                        placeholder="选择时间">
                    </el-time-picker>
                  </el-form-item>
                  <el-form-item prop="address" label="事件地点">
                    <el-input v-model="item.address" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item prop="description" label="事件描述">
                    <el-input v-model="item.description" autocomplete="off"></el-input>
                  </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="hideModifyBar(index)" style="height: 50%">取消</el-button>
                  <el-button type="primary" @click="save(item)" style="height: 50%">确认</el-button>
                </div>
              </el-dialog>
              <el-dialog title="消息" :visible.sync="unaffordable" width="40%" :close-on-click-modal="false" destroy-on-close>
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                余额不足，请充值后重试。
              </div>
              <div slot="footer" class="dialog-footer">
                <el-button @click="unaffordable = false" style="height: 50%">确认</el-button>
              </div>
            </el-dialog>
            </div>
          </el-card>
        </el-col>
      </el-row>
      </div>

      <div class="pagination" style="margin-top: 15px">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      eventData: [],
      pageNum: 1,   // 当前的页码
      pageSize: 12,  // 每页显示的个数
      total: 0,
      currentRepository: {},
      addConfirmVisible: false,
      currentDate: new Date(),
      purchaseVisible: false,
      modifyVisible: false,
      unaffordable: false,
      deleteConfirmVisible: false,
      eventName: null,
      addEvent:{},
      addVisible: false,
      categoryData: [],
      displayLast: new Date(2016, 9, 0, 0, 0),
    }
  },
  mounted() {
  },
  created() {
    this.load(1)
    this.loadCategory()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    hideDeleteBar(index){
      this.$set(this.eventData[index], 'deleteConfirmVisible', false)
    },
    hidePurchaseBar(index){
      this.$set(this.eventData[index], 'purchaseVisible', false)
    },
    hideModifyBar(index){
      this.$set(this.eventData[index], 'modifyVisible', false)
    },
    HandleDeleteFromRepository(index){
      this.$set(this.eventData[index], 'deleteConfirmVisible', true)
    },
    handleChange() {
      let value = this.displayLast.toLocaleTimeString()
      // 假设 value 是 "HH:mm:ss" 格式的时间字符串
      let timeArray = value.split(':');
      let hours = parseInt(timeArray[0]);
      let minutes = parseInt(timeArray[1]);
      let seconds = parseInt(timeArray[2]);

      // 更新 item.last 的值
      this.addEvent.last = hours * 3600 + minutes * 60 + seconds;
    },
    handleImageSuccess(item, response, file, fileList) {
      this.addEvent.img = response.data
    },
    saveAdd(item) {   // 保存按钮触发的逻辑  它会触发新增或者更新
      item.addVisible = false
      item.status = "REPOSITORY"
      this.$request({
        url: item.id ? '/event/update' : '/event/addInRepository?userId='+this.user.id,
        method: item.id ? 'PUT' : 'POST',
        data: item
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
      this.load(1)
    },
    loadCategory(){
      this.$request.get('eventCategory/selectAll').then(res=>{
        if(res.code === '200'){
          this.categoryData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url){
      location.href=url
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    HandleDelete(item){
      this.currentRepository = {}
      this.currentRepository.userId = this.user.id
      this.currentRepository.eventId = item.id
      item.deleteConfirmVisible = false
      if(item.status === 'STATIC'){
        this.$request.delete('/repository/delete?userId=' + this.user.id+'&eventId='+item.id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      } else {
        this.$request.delete('/repository/deleteWithEvent?userId=' + this.user.id+'&eventId='+item.id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }
      this.load(1)
    },
    HandleAddToSchedule(index){
      if(this.eventData[index].price > 0){
        this.$set(this.eventData[index], 'purchaseVisible', true)
      } else {
        this.$set(this.eventData[index], 'modifyVisible', true)
      }
    },
    purchase(item){
      item.purchaseVisible = false
      if(item.price === 0){
        item.modifyVisible = true
      }else{
          if(item.price <= this.user.money){
          this.user.money -= item.price
          let jsonUser = JSON.stringify(this.user);
          localStorage.setItem('xm-user', jsonUser);
          this.$request({
            url: this.user.id ? '/user/update' : '/user/add',
            method: this.user.id ? 'PUT' : 'POST',
            data: this.user
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
            item.modifyVisible = true
        } else {
            item.unaffordable = true
        }
      }
    },
    reset() {
      this.eventName = null
      this.load(1)
    },
    save(item){
      let initialId = item.id
      item.modifyVisible = false
      item.id = null
      item.status = 'PERSONAL'
      this.$request({
        url: '/event/applyInSchedule?userId='+this.user.id+'&date='+this.currentDate.toLocaleDateString()+'&initialId='+initialId,
        method: 'POST',
        data: item
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/event/selectRepositoryPage/'+this.user.id, {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.eventName
        }
      }).then(res => {
        this.eventData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
  }
}
</script>