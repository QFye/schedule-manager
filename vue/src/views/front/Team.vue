<template>
  <div class="main-content">
    <div style="background-color: white;padding: 15px 20px;height: 45px">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/front/teamHome'}">我的团队</el-breadcrumb-item>
        <el-breadcrumb-item>{{teamData.name}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="width: 70%;background-color: white; min-height: 200px; margin:15px auto; border-radius: 20px; padding: 25px 15px">
      <div style="padding: 10px 10px;border-bottom: #cccccc 1px solid">
        <div style="text-align: center;font-size: 35px;font-weight: bold">
          {{teamData.name}}
        </div>
        <div style="text-align: center;font-size: 20px">
          团队编号：{{teamData.id}}
        </div>
      </div>

      <div style="padding: 10px 10px">
        <div style="display: flex">
          <div style="font-weight: bold;font-size: 25px;margin-bottom: 10px;margin-left: 5px;width: 50%">团队介绍</div>
          <div style="width: 50%;text-align: right" v-if="teamData.userId === this.user.id">
            <el-button type="primary" plain @click="ModifyDescription">
              修改
            </el-button>
          </div>
        </div>
        <div style="min-height: 200px;padding: 10px 10px;background-color: #f8f8f8;border-radius: 5px">
          {{teamData.description}}
        </div>

        <el-dialog title="修改团队介绍" :visible.sync="this.modifyDescriptionBar" width="40%" :close-on-click-modal="false" destroy-on-close>
          <el-form :model="teamData" label-width="0px" ref="formRef">
            <el-form-item prop="description">
              <div id="editor" style="width:100%"></div>
            </el-form-item>
          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="cancelModify">取 消</el-button>
            <el-button type="primary" @click="saveModify">确 定</el-button>
          </div>
        </el-dialog>
      </div>

      <div style="padding: 10px 10px;display: flex">
        <div style="width:50%">
          <div style="font-weight: bold;font-size: 25px;margin-bottom: 10px;margin-left: 5px">成员列表</div>
          <div style="font-size: 18px;margin-bottom: 15px;margin-left: 5px">成员数量：{{this.memberCount}}</div>
          <div style="text-align: center">
            <template>
              <ul class="infinite-list" style="overflow:auto;width:90%; height: 200px;background-color: #f8f8f8;border-radius: 5px;padding: 10px 10px" infinite-scroll-immediate="true">
                <li v-for="item in memberData" class="infinite-list-item">
                  <div style="display: flex;margin: 10px 5px">
                    <div style="width: 10%;font-size: 10px;text-align: center;background-color: orange;color: white;margin: auto 0;border-radius: 5px">
                      <div v-if="item.id === teamData.userId">
                        管理员
                      </div>
                      <div v-else style="background-color: #80d89a;border-radius: 5px">
                        成员
                      </div>
                    </div>
                    <div style="width:20%;margin: auto 0">
                      <img :src="item.avatar" style="border-radius: 5px; height: 30px; width: 35%;margin: auto 0">
                    </div>
                    <div style="width: 50%;text-align: left;margin: auto 0">
                      {{item.name}}
                    </div>
                    <div v-if="checkCurrent(item)">
                      <el-button type="danger" plain style="height: 30px" @click="RemoveMember(item)">移除</el-button>
                    </div>
                  </div>
                </li>
              </ul>
            </template>
          </div>
        </div>

        <div style="width: 50%">
          <div style="font-weight: bold;font-size: 25px;margin-bottom: 10px;margin-left: 5px">我的日程仓库</div>
          <div style="font-size: 18px;margin-bottom: 15px;margin-left: 5px">事件数量：{{this.eventCount}}</div>
          <div style="text-align: center">
            <template>
              <ul class="infinite-list" style="overflow:auto;width:100%; height: 200px;background-color: #f8f8f8;border-radius: 5px;padding: 10px 10px" infinite-scroll-immediate="true">
                <li v-for="(item, index) in repositoryData" class="infinite-list-item">
                  <div style="display: flex;margin: 10px 5px">
                    <div style="width: 50%;text-align: left;margin: auto 0">
                      {{item.name}}
                    </div>
                    <div style="width: 50%;text-align: right;margin: auto 0">
                      <el-button type="primary" plain @click="ConfirmSend(index)">发送给成员</el-button>
                    </div>
                  </div>

                  <div style="text-align: left">
                    <el-dialog title="修改并添加至日程表" :visible.sync="item.sendVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                    <el-form label-width="100px" style="padding-right: 50px" :model="item" ref="formRef">
                      <el-form-item prop="name" label="事件名称">
                        <el-input v-model="item.name" autocomplete="off"></el-input>
                      </el-form-item>
                      <el-form-item prop="date" label="日期">
                        <el-date-picker
                            v-model="item.date"
                            type="date"
                            placeholder="选择日期">
                        </el-date-picker>
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
                      <el-button @click="HideSend(index)" style="height: 50%">取消</el-button>
                      <el-button type="primary" @click="SendEvent(index)" style="height: 50%">确认</el-button>
                    </div>
                  </el-dialog>
                  </div>

                </li>
              </ul>
            </template>
          </div>
        </div>

      </div>

      <div style="text-align: right;margin-top: 10px">
        <el-button type="primary" plain>新建事件并发送</el-button>
      </div>

      <div style="padding: 10px 10px" v-if="isCurrentUserManager">
        <div>
          <div style="font-weight: bold;font-size: 25px;margin-bottom: 10px;margin-left: 5px;width: 50%">加入申请</div>
          <div style="min-height: 200px;padding: 10px 10px;background-color: #f8f8f8;border-radius: 5px;width:100%">
            <template>
              <ul class="infinite-list" style="overflow:auto;width:100%; height: 200px;background-color: #f8f8f8;border-radius: 5px;padding: 10px 10px" infinite-scroll-immediate="true">
                <div style="display: flex;margin: 10px 5px;font-weight: bold">
                  <div style="width: 75%;text-align: left;margin: auto 0;display: flex; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                    <div style="width: 40%;text-align: center">用户名</div>
                    <div style="width: 60%;text-align: center">申请时间</div>
                  </div>
                  <div style="width: 25%;text-align: right;margin: auto 0">
                    <div style="width: 100%;text-align: center">操作</div>
                  </div>
                </div>
                <li v-for="item in applicationData" class="infinite-list-item">
                  <div style="display: flex;margin: 10px 5px">
                    <div style="width: 75%;text-align: left;margin: auto 0;display: flex; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                      <div style="width: 40%;text-align: center">{{item.name}}</div>
                      <div style="width: 60%;text-align: center">{{item.time | extractDate}} {{item.time | extractTime}}</div>
                    </div>
                    <div style="width: 25%;text-align: center;margin: auto 0" v-if="item.status === 0">
                      <el-button type="success" plain @click="Accept(item)">通过</el-button>
                      <el-button type="danger" plain @click="Refuse(item)">拒绝</el-button>
                    </div>
                    <div style="width: 25%;text-align: center;margin: auto 0" v-else-if="item.status === 1">
                      <el-button type="info" plain disabled>已通过</el-button>
                    </div>
                    <div style="width: 25%;text-align: center;margin: auto 0" v-else>
                      <el-button type="info" plain disabled>已拒绝</el-button>
                    </div>
                  </div>
                </li>
              </ul>
            </template>
          </div>
          <div style="text-align: right;margin-top: 20px">
            <el-button type="primary" plain @click="Empty()">清空申请</el-button>
          </div>
        </div>
      </div>

      <div style="padding: 10px 10px" v-if="isCurrentUserManager">
        <div>
          <div style="font-weight: bold;font-size: 25px;margin-bottom: 10px;margin-left: 5px;width: 50%">团队管理</div>
          <div style="padding: 20px 20px;font-weight: bold">
            解散团队：
            <el-button type="danger" plain>解散团队</el-button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import E from 'wangeditor'

let editor
function initWangEditor(content){
  setTimeout(()=>{
    if(!editor){
      editor = new E("#editor")
      editor.config.placeholder = "请输入内容"
      editor.config.uploadFileName = "file"
      editor.config.uploadImgServer = "http://localhost:9090/files/wang/upload"
      editor.create()
    }
    editor.txt.html(content)
  }, 0)
}

export default {

  data() {
    let teamId = this.$route.query.id
    return {
      teamId: teamId,
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      activeName: 'join',
      teamData: {},
      memberData: [],
      repositoryData: [],
      applicationData: [],
      memberCount: 0,
      eventCount: 0,
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      infoVisible: false,
      isCurrentUserManager: false,
      modifyDescriptionBar: false,
      firstInit: true,
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
    this.loadMember()
    this.loadRepository()
    this.loadApplication()
    this.load(1)
    this.loadForm()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    ConfirmSend(index){
      this.$set(this.repositoryData[index], 'sendVisible', true)
    },
    HideSend(index){
      this.$set(this.repositoryData[index], 'sendVisible', false)
    },
    SendEvent(index){
      let item = this.repositoryData[index]
      item.status = 'UNCONFIRMED'
      this.$request({
        url: '/event/sendEvent/'+this.teamData.id+'?currentUserId='+this.user.id,
        method: 'POST',
        data: item
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('发送成功')
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    checkCurrent(item){
      if(this.isCurrentUserManager){
        if(item.id === this.user.id){
          return false
        } else {
          return true
        }
      }
      return false
    },
    RemoveMember(item){
      this.$confirm('您确定移出成员 '+item.name+' 吗？', '确认', {type: "warning"}).then(response => {
        this.$request.delete('/team/deleteFromTeam?teamId=' + this.teamData.id+'&userId='+item.id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    Empty(){
      this.$confirm('您确定清空申请吗？', '确认', {type: "warning"}).then(response => {
        this.$request.delete('/joinApplication/deleteByTeam/' + this.teamData.id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    Refuse(item){
      item.status = 2
      this.$request({
        url: item.id ? '/joinApplication/update' : '/joinApplication/add',
        method: item.id ? 'PUT' : 'POST',
        data: item
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('已拒绝该申请')
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    Accept(item){
      item.status = 1
      this.$request({
        url: '/joinApplication/accept',
        method: 'PUT',
        data: item
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('已通过该申请')
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    cancelModify(){
      this.modifyDescriptionBar = false
    },
    ModifyDescription(row){
      this.form = JSON.parse(JSON.stringify(row))
      if(this.firstInit) {
        initWangEditor()
        this.firstInit = false
      }
      this.modifyDescriptionBar = true
    },
    saveModify(){
      this.teamData.description = editor.txt.html()
      if(this.teamData.description === ''){
        this.teamData.description = '该团队没有设置简介哦~'
      }
      this.$request({
        url: '/team/update',
        method: 'PUT',
        data: this.teamData
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.modifyDescriptionBar = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    loadForm(){
      this.form.userId = this.user.id
      this.form.creationTime = new Date()

    },
    loadRepository(){
      this.$request.get('event/selectAllFromRepository/'+this.user.id).then(res=>{
        if(res.code == '200'){
          this.repositoryData = res.data
          this.eventCount = res.data.length
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadApplication(){
      this.$request.get('joinApplication/selectAll',{
        params:{
          teamId: this.teamData.id
        }
      }).then(res=>{
        if(res.code == '200'){
          this.applicationData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
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
      this.$request.get('team/selectById/'+this.teamId).then(res=>{
        if(res.code === '200'){
          this.teamData = res.data
          if(this.teamData.userId === this.user.id){
            this.isCurrentUserManager = true
          }
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadMember(){
      this.$request.get('user/selectByTeam/'+this.teamId).then(res=>{
        if(res.code == '200'){
          this.memberData = res.data
          this.memberCount = res.data.length
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