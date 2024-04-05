<template>
  <div class="main-content">
    <div style="width: 60%; display: flex;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <el-row gutter="20">
        <el-col style="text-align: center; font-size: 18px;background-color: #bbcffc; height: 30px; width: 90px; margin: 10px 20px;border-radius: 15px; display: flex; padding-left: 8px">
          商家信息
        </el-col>
        <el-col style="width: 100%;display: flex; margin: auto 100px">
          <div style="height: 120px; width: 16%">
            <img :src="businessData.avatar" style="height: 120px;width: 120px;border-radius: 60px">
          </div>
          <div style="width: 40%;">
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;height: 30px; min-width:200px;line-height: 30px;font-size: 25px;font-weight: bold; margin-left: 80px;margin-right: 20px;margin-bottom: 10px">{{businessData.name}}</div>
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;height: 30px; width:200px;line-height: 30px;font-size: 15px;margin: 2.5px 80px">联系电话：{{businessData.phone}}</div>
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;height: 30px; width:200px;line-height: 30px;font-size: 15px;margin: 2.5px 80px">邮箱：{{businessData.email}}</div>
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;height: 30px; width:200px;line-height: 30px;font-size: 15px;margin: 2.5px 80px">地址：{{businessData.address}}</div>
          </div>
          <div style="width: 44%;margin-right: 30px">
            <div style="margin: 28px auto; text-align: center; background-color: #7fa0df; color: white;width:25%;height: 30px;line-height: 30px;border-radius: 5px"><a href="#" @click="infoVisible = true">发消息</a></div>
            <div style="margin: 28px auto; text-align: center; background-color: #7fa0df; color: white;width:25%;height: 30px;line-height: 30px;border-radius: 5px"><a href="#" @click="movToFavourites()">收藏商家</a></div>
          </div>
          <el-dialog title="消息" :visible.sync="infoVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
              该功能还在开发中哦~
            </div>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="infoVisible = false" style="height: 50%">确认</el-button>
            </div>
          </el-dialog>

        </el-col>
        <el-col style="margin: 0px 10px">
          <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;text-align: center; font-size: 18px;background-color: #bbcffc; height: 30px; width: 90px; margin: 10px 0px;border-radius: 15px; display: flex; padding-left: 8px">
            商家简介
          </div>
        </el-col>
        <el-col v-html="businessData.description" style="min-height: 30px; width:100%;line-height: 30px;font-size: 15px;margin: 0 18px">

        </el-col>
      </el-row>
    </div>
    <div style="width: 60%; background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 20px">
      <el-row :gutter="20">
        <el-col v-for="item in eventData" :span="6" style="width: 20%">
          <div style="width: 100%; object-fit: contain; height: 140px">
            <img @click="navTo('/front/eventDetail?id=' + item.id)" :src="item.img" style="width: 100%; height: 140px; border-radius: 10px">
          </div>
          <div style="font-weight: 500; margin-top: 0px; text-overflow: ellipsis; overflow: hidden; width: 100%;white-space: nowrap; margin-top: 10px">
            {{ item.name }}</div>
          <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; width: 100%;margin-top: 5px;color: red;font-weight: bold">￥{{ item.price }}</div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    let businessId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      businessId: businessId,
      businessData: {},
      eventData: [],
      infoVisible: false
    }
  },
  mounted() {
    this.loadBusiness()
    this.loadEvent()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    navTo(url){
      location.href=url
    },
    movToFavourites(){
      let data = {userId: this.user.id, businessId: this.businessData.id}
      this.$request.post('/collect/add', data).then(res=>{
        if(res.code === '200'){
          this.$message.success('收藏成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadBusiness(){
      this.$request.get('business/selectById?id='+this.businessId).then(res=>{
        if(res.code === '200'){
          this.businessData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadEvent(){
      this.$request.get('event/selectByBusinessId?id='+this.businessId).then(res=>{
        if(res.code === '200'){
          this.eventData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>