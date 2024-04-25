<template>
  <div class="main-content">
    <div style="background-color: white;padding: 15px 20px;height: 45px">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>我的团队</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="width: 60%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 25px 15px">
      <div style="text-align: center; font-weight: bold; font-size: 30px;width:100%;height: 30px;margin-bottom: 15px">
        我的团队
      </div>


      <div style="padding: 5px 5px; display: flex">
        <el-button type="primary" style="margin: 0 10px" @click="fromVisible = true">
          创建团队
        </el-button>
        <el-button type="primary" style="margin: 0 10px" @click="joinVisible = true">
          加入团队
        </el-button>

        <el-dialog title="加入团队" :visible.sync="joinVisible" width="25%" :close-on-click-modal="false" destroy-on-close>
          <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
            <el-input v-model="joinTeam" placeholder="请输入队伍编号"></el-input>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button @click="joinVisible = false" style="height: 50%">取消</el-button>
            <el-button type="primary" @click="FindTeam" style="height: 50%">确认</el-button>
          </div>
        </el-dialog>

        <el-dialog title="信息" :visible.sync="confirmVisible" width="25%" :close-on-click-modal="false" destroy-on-close>
          <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
            是否确认加入团队 {{result.name}} ？
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button @click="confirmVisible = false" style="height: 50%">取消</el-button>
            <el-button type="primary" @click="SendApplication" style="height: 50%">确认</el-button>
          </div>
        </el-dialog>

        <el-dialog title="信息" :visible.sync="alreadyIn" width="25%" :close-on-click-modal="false" destroy-on-close>
          <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
            已加入该团队。
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="alreadyIn = false" style="height: 50%">确认</el-button>
          </div>
        </el-dialog>
      </div>
      <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="name" label="团队名称">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="creationTime" label="创建时间">
            <el-input v-model="form.creationTime" autocomplete="off" disabled="true"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="消息" :visible.sync="infoVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
          该功能还在开发中哦~
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="infoVisible = false" style="height: 50%">确认</el-button>
        </div>
      </el-dialog>

      <div style="margin-top: 5px; padding: 5px 20px">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="我加入的团队" name="join">
            <div style="">
              <div style="display: flex;border-bottom: #cccccc 1px solid;padding: 0 20px" v-for="(item, index) in teamData">
                <div style="width:80%">
                  <div style="font-weight: bold; font-size: 20px; margin-bottom: 10px; margin-top: 10px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                    团队名称：{{item.name}}
                  </div>
                  <div style="margin: 2.5px 0; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                    创建用户：{{item.user}}
                  </div>
                  <div style="margin-top: 2.5px; margin-bottom: 5px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                    创建时间：{{item.creationTime | extractDate}} {{item.creationTime | extractTime}}
                  </div>
                </div>

                <div style="width:20%;padding: 15px 0">
                  <div style="display: flex; flex-direction: column; gap: 10px; text-align: right;">
                    <el-button type="primary" plain style="width: 60%;margin-left: 10px" @click="navTo('/front/team?id='+item.id)">
                      团队功能
                    </el-button>
                    <el-button type="danger" plain style="width: 60%" @click="HandleDeleteFromTeam(index)">
                      退出团队
                    </el-button>
                    <div style="text-align: left;">
                      <el-dialog title="退出团队" :visible.sync="item.deleteConfirmVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                          即将退出团队 {{item.name}} ，是否继续？
                        </div>
                        <div slot="footer" class="dialog-footer">
                          <el-button @click="hideDeleteBar(index)" style="height: 50%">取消</el-button>
                          <el-button type="primary" @click="HandleDelete(item)" style="height: 50%">确认</el-button>
                        </div>
                      </el-dialog>
                    </div>

                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我创建的团队" name="create">
            <div style="">
              <div style="display: flex;border-bottom: #cccccc 1px solid;padding: 0 20px" v-for="(item, index) in managerData">
                <div style="flex: 1">
                  <div style="font-weight: bold; font-size: 20px; margin-bottom: 10px; margin-top: 10px">
                    团队名称：{{item.name}}
                  </div>
                  <div style="margin: 2.5px 0">
                    成员人数：{{item.memberCount}}
                  </div>
                  <div style="margin-top: 2.5px; margin-bottom: 5px">
                    创建时间：{{item.creationTime | extractDate}} {{item.creationTime | extractTime}}
                  </div>
                </div>

                <div style="width:20%;padding: 15px 0">
                  <div style="display: flex; flex-direction: column; gap: 10px; text-align: right;">
                    <el-button type="primary" plain style="width: 60%;margin-left: 10px" @click="navTo('/front/team?id='+item.id)">
                      团队功能
                    </el-button>
                    <el-button type="danger" plain style="width: 60%" @click="HandleDeleteFromTeam2(index)">
                      退出团队
                    </el-button>
                    <div style="text-align: left;">
                      <el-dialog title="退出团队" :visible.sync="item.deleteConfirmVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                          即将退出团队 {{item.name}} ，是否继续？
                        </div>
                        <div slot="footer" class="dialog-footer">
                          <el-button @click="hideDeleteBar2(index)" style="height: 50%">取消</el-button>
                          <el-button type="primary" @click="HandleDelete(item)" style="height: 50%">确认</el-button>
                        </div>
                      </el-dialog>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

    </div>
  </div>
</template>

<script>


export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      activeName: 'join',
      teamData: [],
      managerData: [],
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      infoVisible: false,
      deleteTeam: {},
      joinVisible: false,
      joinTeam: '',
      result: {},
      confirmVisible: false,
      alreadyIn: false,
      newData: {},
      InTeam: true,
    }
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
  mounted() {
    this.loadTeam()
    this.loadManager()
    this.load(1)
    this.loadForm()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    SendApplication(){
      let newData = {}
      newData.userId = this.user.id
      newData.teamId = parseInt(this.joinTeam)
      this.confirmVisible = false
      this.$request.get('team/selectRelation?userId='+newData.userId+'&teamId='+newData.teamId).then(res=>{
        if(res.code == '200'){
          if(res.data !== null){
            this.alreadyIn = true
          }
        }else{
          this.$message.error(res.msg)
        }
      })
      if(this.alreadyIn !== true){
        this.$request({
          url: '/joinApplication/add',
          method: 'POST',
          data: newData
        }).then(res => {
          if (res.code === '200') {  // 表示成功保存
            this.$message.success('已发送加入申请')
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }
    },
    FindTeam(){
      this.$request.get('team/selectById/'+this.joinTeam).then(res=>{
        if(res.code == '200'){
          if(res.data === null){
            this.$message.info('没有查询到相关团队')
          } else {
            this.result = res.data
            this.joinVisible = false
            this.confirmVisible = true
          }
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    HandleDelete(item){
      this.deleteTeam = {}
      this.deleteTeam.userId = this.user.id
      this.deleteTeam.teamId = item.id
      item.deleteConfirmVisible = false
      this.$request.delete('/team/deleteFromTeam?userId=' + this.user.id+'&teamId='+item.id).then(res => {
        if (res.code === '200') {   // 表示操作成功
          this.$message.success('操作成功')
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    HandleDeleteFromTeam(index){
      this.$set(this.teamData[index], 'deleteConfirmVisible', true)
    },
    hideDeleteBar(index){
      this.$set(this.teamData[index], 'deleteConfirmVisible', false)
    },
    HandleDeleteFromTeam2(index){
      this.$set(this.managerData[index], 'deleteConfirmVisible', true)
    },
    hideDeleteBar2(index){
      this.$set(this.managerData[index], 'deleteConfirmVisible', false)
    },
    loadForm(){
      this.form.userId = this.user.id
      this.form.creationTime = new Date()

    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/team/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    navTo(url){
      location.href=url
    },
    loadTeam(){
      this.$request.get('team/selectByUserId?id='+this.user.id).then(res=>{
        if(res.code == '200'){
          this.teamData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadManager(){
      this.$request.get('team/selectByManagerId?id='+this.user.id).then(res=>{
        if(res.code == '200'){
          this.managerData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleClick(tab, event){

    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/team/update' : '/team/add?userId='+this.form.userId,
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
  }
}
</script>