<template>
  <div class="main-content">
    <div style="width: 60%; background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <div style="height: 30px; line-height: 30px; text-align: center; display: flex">
        <div style="; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">选择日期：</div>
        <el-date-picker
            v-model="currentDate"
            type="date"
            placeholder="选择日期">
        </el-date-picker>
        <el-button style="margin-left: 10px" @click="loadSchedule">切换</el-button>
        <el-button type="primary" @click="addVisible = true">添加</el-button>
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
          <el-form-item prop="name" label="持续时间">
            <el-input v-model="addEvent.last" autocomplete="off"></el-input>
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
          <el-button type="primary" @click="save(addEvent)">确 定</el-button>
        </div>
      </el-dialog>
      <div style="margin-top: 20px; padding: 10px 10px 10px 10px">
        <h3 style="margin-bottom: 8px">当前日期：{{ showDate.toLocaleDateString() }}</h3>
        <el-timeline>
          <el-timeline-item :timestamp="item.start | extractTime" placement="top" v-for="(item, index) in scheduleData" :key="item.id">
            <el-card>
              <div style="display: flex">
                <div style="width: 12%;text-align: center">
                  <img :src="item.img" style="width: 80%; height: 72px; border: #cccccc 2px solid;background-color: #c8d6f1">
                </div>
                <div style="width: 30%;border-right: #cccccc 2px solid;padding-left: 16px">
                    <h3 style="margin-top: 5px; margin-bottom: 2.5px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{ item.name }}</h3>
                  <p style="margin-top: 2.5px; margin-bottom: 2.5px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">地点：{{item.address}}</p>
                  <p style="margin-top: 2.5px; margin-bottom: 5px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">类别：{{item.categoryName}}</p>

                  <el-dialog title="删除" :visible.sync="item.deleteConfirmVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                    <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                      事件 {{item.name}} 删除后将无法恢复，是否确认删除？
                    </div>
                    <div slot="footer" class="dialog-footer">
                      <el-button @click="hideDeleteBar(index)" style="height: 50%">取消</el-button>
                      <el-button type="primary" @click="del(item)" style="height: 50%">确认</el-button>
                    </div>
                  </el-dialog>
                  <el-dialog title="信息" :visible.sync="item.editVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
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
                      <el-button @click="hideEditBar(index)">取 消</el-button>
                      <el-button type="primary" @click="save(item)">确 定</el-button>
                    </div>
                  </el-dialog>
                </div>
                <div style="width: 48%; padding-left: 16px">
                  <div style="padding-top: 10px;padding-bottom: 10px">
                    简介：{{item.description}}
                  </div>
                </div>
                <div style="margin-top: 10px; margin-bottom: 5px; width: 10%">
                  <div style="margin-bottom: 8px;text-align: center">
                    <el-button type="primary" icon="el-icon-edit" style="width: 70%" @click="showEditBar(index)"></el-button>
                  </div>
                  <div style="text-align: center">
                    <el-button type="primary" icon="el-icon-delete" style="width:70%" @click="showDeleteBar(index)"></el-button>
                  </div>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      scheduleData: [],
      currentDate: new Date(),
      showDate: this.currentDate,
      addEvent: {},
      addVisible: false,
      categoryData: [],
      deleteConfirmVisible: {},
      editVisible: {},
    }
  },
  mounted() {
    this.loadSchedule()
    this.loadEvent()
    this.loadCategory()
  },
  filters: {
    extractDate(datetime) {
      // 使用JavaScript的Date对象解析datetime字符串
      const dateObject = new Date(datetime);

      // 提取日期部分
      const year = dateObject.getFullYear();
      const month = ("0" + (dateObject.getMonth() + 1)).slice(-2);
      const day = ("0" + dateObject.getDate()).slice(-2);

      // 返回格式化的日期字符串
      return `${year}-${month}-${day}`;
    },

    extractTime(datetime) {
      // 同样解析datetime字符串
      const dateObject = new Date(datetime);

      // 提取时间部分
      const hours = ("0" + dateObject.getHours()).slice(-2);
      const minutes = ("0" + dateObject.getMinutes()).slice(-2);
      const seconds = ("0" + dateObject.getSeconds()).slice(-2);

      // 返回格式化的时间字符串
      return `${hours}:${minutes}:${seconds}`;
    }
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    showDeleteBar(index){
      this.$set(this.scheduleData[index], 'deleteConfirmVisible', true)
    },
    showEditBar(index){
      this.$set(this.scheduleData[index], 'editVisible', true)
    },
    hideDeleteBar(index){
      this.$set(this.scheduleData[index], 'deleteConfirmVisible', false)
    },
    hideEditBar(index){
      this.$set(this.scheduleData[index], 'editVisible', false)
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
    del(item) {   // 单个删除
      item.deleteConfirmVisible = false
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/event/deleteInSchedule/' + item.id+'?userId='+this.user.id+'&date='+this.showDate.toLocaleDateString()).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    loadEvent(){
      this.addEvent.status = 'PERSONAL'
    },
    save(item) {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.addVisible = false
      item.editVisible = false
      this.$request({
        url: item.id ? '/event/update' : '/event/addInSchedule?userId='+this.user.id+'&date='+this.showDate.toLocaleDateString(),
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
      this.loadSchedule()
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/event/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleImageSuccess(response, file, fileList) {
      this.addEvent.img = response.data
    },
    navTo(url){
      location.href=url
    },
    toggleToToday() {
      // 更新currentDate为当前日期并格式化
      this.currentDate = new Date().toLocaleDateString();
    },
    loadSchedule(){
      this.showDate = this.currentDate
      this.$request.get('event/selectByUserAndDate?id='+this.user.id+'&date='+this.currentDate.toLocaleDateString()).then(res=>{
        if(res.code == '200'){
          this.scheduleData = res.data
          this.scheduleData.editVisible = false
          this.scheduleData.deleteConfirmVisible = false
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    calculateEndTime(item) {
      let startDate = new Date(item.start);
      let endDate = new Date(startDate.getTime() + item.last * 1000);

      // 将结束时间转换为HH:MM:SS格式
      let hours = String(endDate.getHours()).padStart(2, '0');
      let minutes = String(endDate.getMinutes()).padStart(2, '0');
      let seconds = String(endDate.getSeconds()).padStart(2, '0');

      return `${hours}:${minutes}:${seconds}`;
    },
  }
}
</script>