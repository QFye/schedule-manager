<template>
  <div class="main-content">
    <div style="width: 60%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 25px 15px">
      <div style="text-align: center; font-weight: bold; font-size: 30px;width:100%;height: 30px;margin-bottom: 15px">
        我的团队
      </div>


      <div style="padding: 5px 5px; display: flex">
        <el-button type="primary" style="margin: 0 10px" @click="fromVisible = true">
          创建团队
        </el-button>
        <el-button type="primary" style="margin: 0 10px" @click="infoVisible = true">
          查找团队
        </el-button>
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
              <div style="display: flex;border-bottom: #cccccc 1px solid;padding: 0 20px" v-for="item in teamData">
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
                    <el-button type="primary" plain style="width: 60%;margin-left: 10px" @click="infoVisible = true">
                      团队功能
                    </el-button>
                    <el-button type="danger" plain style="width: 60%" @click="infoVisible = true">
                      退出团队
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我创建的团队" name="create">
            <div style="">
              <div style="display: flex;border-bottom: #cccccc 1px solid;padding: 0 20px" v-for="item in managerData">
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
                    <el-button type="primary" plain style="width: 60%;margin-left: 10px" @click="infoVisible = true">
                      团队功能
                    </el-button>
                    <el-button type="danger" plain style="width: 60%" @click="infoVisible = true">
                      退出团队
                    </el-button>
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