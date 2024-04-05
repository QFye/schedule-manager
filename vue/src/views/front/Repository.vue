<template>
  <div class="main-content">
    <div style="width: 80%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <div style="font-size: 30px; font-weight: bold; text-align: center; width: 100%; line-height: 50px; height: 50px; border-bottom: #cccccc 2px solid; margin-bottom: 12px">
        日程仓库
      </div>
      <div>
      <el-row style="padding: 0 10px" :gutter="20">
        <el-col v-for="item in eventData" :span="5" style="width: 25%">
          <el-card style="width: 100%; padding: 5px 2.5px; margin-top:10px">
            <div style="height: 215px; border-radius: 10px;width: 100%;object-fit: contain;line-height: 180px;text-align: center; margin: 0 auto">
              <img :src="item.img" class="image" style="border-radius: 10px;width: 100%; height: 100%; margin: 0 auto;object-fit: contain" @click="navTo('/front/eventDetail?id=' + item.id)">
            </div>
            <div style="padding: 14px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
              <span style=";font-weight: bold" @click="navTo('/front/eventDetail?id=' + item.id)">{{ item.name }}</span>
              <div class="bottom clearfix">
                <time class="time" style="; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{ item.categoryName }}</time>
              </div>
              <div @click="addConfirmVisible = true">
                <el-button type="text" class="button">添加至今日计划表</el-button>
              </div>
              <el-dialog title="添加至日程表" :visible.sync="addConfirmVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                  当前事件 {{item.name}} 将添加至今日计划表，是否继续？
                </div>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="addConfirmVisible = false" style="height: 50%">取消</el-button>
                  <el-button type="primary" @click="HandleAdd(item)" style="height: 50%">确认</el-button>
                </div>
              </el-dialog>
              <el-dialog title="支付金额" :visible.sync="purchaseVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; padding: 0px 20px">
                  支付金额：{{item.price}}<br>
                  当前余额：{{user.money}}<br>
                  是否继续？
                </div>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="purchaseVisible = false" style="height: 50%">取消</el-button>
                  <el-button type="primary" @click="purchase(item)" style="height: 50%">确认</el-button>
                </div>
              </el-dialog>
              <el-dialog title="添加至日程表" :visible.sync="modifyVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                  当前事件 {{item.name}} 将添加至今日计划表，是否继续？
                </div>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="modifyVisible = false" style="height: 50%">取消</el-button>
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
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      eventData: [],
      addConfirmVisible: false,
      currentDate: new Date(),
      purchaseVisible: false,
      modifyVisible: false,
      unaffordable: false,
    }
  },
  mounted() {
    this.loadEvent()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    navTo(url){
      location.href=url
    },
    loadEvent(){
      this.$request.get('event/selectPersonal?id='+this.user.id).then(res=>{
        if(res.code == '200'){
          this.eventData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    HandleAdd(item){
      this.addConfirmVisible = false
      if(item.price === 0){
        this.modifyVisible = true
      } else {
        this.purchaseVisible = true
      }
    },
    purchase(item){
      this.purchaseVisible = false
      if(item.price <= this.user.money){
        this.user.money -= this.eventData.price
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
        this.modifyVisible = true
      } else {
        this.unaffordable = true
      }
    },
    save(item){
      item.id = null
      item.status = 'PERSONAL'
      this.$request({
        url: '/event/addInSchedule?userId='+this.user.id+'&date='+this.currentDate.toLocaleDateString(),
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
  }
}
</script>