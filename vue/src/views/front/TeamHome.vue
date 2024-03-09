<template>
  <div class="main-content">
    <div style="width: 60%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 25px 15px">
      <div style="text-align: center; font-weight: bold; font-size: 30px;width:100%;height: 30px;margin-bottom: 15px">
        我的团队
      </div>


      <div style="padding: 5px 5px; display: flex">
        <div style="margin: 0 15px; background-color: #7fa0df; height: 30px;width: 90px;border-radius: 10px;text-align: center;line-height: 30px;color: white">
          查找团队
        </div>
        <div style="margin: 0 15px; background-color: #7fa0df; height: 30px;width: 90px;border-radius: 10px;text-align: center;line-height: 30px;color: white">
          创建团队
        </div>
      </div>

      <div style="margin-top: 5px; padding: 5px 20px">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="我加入的团队" name="join">
            <div style="">
              <div style="display: flex;border-bottom: #cccccc 1px solid;padding: 0 20px" v-for="item in teamData">
                <div style="flex: 1">
                  <div style="font-weight: bold; font-size: 20px; margin-bottom: 10px; margin-top: 10px">
                    团队名称：{{item.name}}
                  </div>
                  <div style="margin: 2.5px 0">
                    创建用户：{{item.user}}
                  </div>
                  <div style="margin-top: 2.5px; margin-bottom: 5px">
                    创建时间：{{item.creationTime}}
                  </div>
                </div>

                <div style="width:200px;padding: 10px 0">
                  <div style="margin: 10px auto; background-color: #7fa0df; height: 25px;width: 80px;border-radius: 10px;text-align: center;line-height: 25px;color: white">
                    团队功能
                  </div>
                  <div style="margin: 10px auto; background-color: #7fa0df; height: 25px;width: 80px;border-radius: 10px;text-align: center;line-height: 25px;color: white">
                    退出团队
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
                    创建时间：{{item.creationTime}}
                  </div>
                </div>

                <div style="width:200px;padding: 10px 0">
                  <div style="margin: 10px auto; background-color: #7fa0df; height: 25px;width: 80px;border-radius: 10px;text-align: center;line-height: 25px;color: white">
                    团队功能
                  </div>
                  <div style="margin: 10px auto; background-color: #7fa0df; height: 25px;width: 80px;border-radius: 10px;text-align: center;line-height: 25px;color: white">
                    退出团队
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
    }
  },
  mounted() {
    this.loadTeam()
    this.loadManager()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
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
  }
}
</script>