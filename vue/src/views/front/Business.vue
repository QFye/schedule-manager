<template>
  <div class="main-content">
    <div style="width: 60%; display: flex;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <el-row gutter="20">
        <el-col style="text-align: center; font-size: 18px;background-color: #bbcffc; height: 30px; width: 90px; margin: 10px 20px;border-radius: 15px; display: flex; padding-left: 8px">
          商家信息
        </el-col>
        <el-col style="display: flex; margin: auto 200px">
          <div style="height: 120px; width: 120px">
            <img :src="businessData.avatar" style="height: 120px;width: 120px;border-radius: 60px">
          </div>
          <div style="flex: 1">
            <div style="height: 30px; min-width:200px;line-height: 30px;font-size: 25px;font-weight: bold; margin-left: 150px;margin-right: 150px;margin-bottom: 10px">{{businessData.name}}</div>
            <div style="height: 30px; width:200px;line-height: 30px;font-size: 15px;margin: 2.5px 150px">联系电话：{{businessData.phone}}</div>
            <div style="height: 30px; width:200px;line-height: 30px;font-size: 15px;margin: 2.5px 150px">邮箱：{{businessData.email}}</div>
            <div style="height: 30px; width:200px;line-height: 30px;font-size: 15px;margin: 2.5px 150px">地址：{{businessData.address}}</div>
          </div>

        </el-col>
        <el-col style="margin: 0px 10px">
          <div style="text-align: center; font-size: 18px;background-color: #bbcffc; height: 30px; width: 90px; margin: 10px 0px;border-radius: 15px; display: flex; padding-left: 8px">
            商家简介
          </div>
        </el-col>
        <el-col v-html="businessData.description" style="min-height: 30px; width:870px;line-height: 30px;font-size: 15px;margin: 0 18px">

        </el-col>
      </el-row>
    </div>
    <div style="width: 60%; display: flex;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 20px">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in eventData">
          <img @click="navTo('/front/eventDetail?id=' + item.id)" :src="item.img" style="width: 170px; height: 160px; border-radius: 10px">
          <div style="font-weight: 500; margin-top: 8px; text-overflow: ellipsis; overflow: hidden; width: 240px;white-space: nowrap">
            {{ item.name }}</div>
          <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; width: 240px;margin-top: 5px;color: red;font-weight: bold">￥{{ item.price }}</div>
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