<template>
  <div class="main-content">
    <div style="background-color: white;padding: 15px 20px;height: 45px">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>我的收藏</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="width: 60%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <div style="display: flex; padding: 20px 280px; border-bottom: #cccccc 1px solid;margin: 0 auto">
        <div style="margin-right: 30px">
          <img src="@/assets/imgs/favourites_icon.png" style="width:100px;height: 100px">
        </div>
        <div style="font-weight: bold;font-size: 35px;height: 100px;line-height: 100px;margin: 0 auto">我的收藏</div>
      </div>

      <div v-for="item in businessData" style="padding: 10px 10px">
        <div style="display: flex; padding-bottom: 10px;border-bottom: #cccccc 1px solid">
          <div style="margin-top: 8px">
            <img :src="item.avatar" style="height: 80px; width: 80px;border-radius: 10px;margin: auto 20px">
          </div>
          <div style="width: 600px;margin-top: 8px">
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;width: 90%;font-weight: bold;font-size: 20px;margin: 2.5px 10px">店铺名称：{{item.name}}</div>
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;width: 90%;font-size: 16px;margin: 2.5px 10px">商家地址：{{item.address}}</div>
            <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;width: 90%;font-size: 16px;margin: 2.5px 10px">商家简介：
              <div v-html="item.description"></div>
            </div>
          </div>
          <div style="flex: 1; margin-right: 20px">
            <div style="height: 30px;line-height: 30px;font-size: 18px;text-align: center;background-color: #7fa0df;width:70%;border-radius: 5px;margin: 10px auto"><a href="#" @click="navTo('/front/business?id='+item.id)">进入店铺</a></div>
            <div style="height: 30px;line-height: 30px;font-size: 18px;text-align: center;background-color: #7fa0df;width:70%;border-radius: 5px;margin: 10px auto"><a href="#" @click="cancelFavourites(item.id)">取消收藏</a></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      businessData: [],
    }
  },
  mounted() {
    this.loadBusiness()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    navTo(url){
      location.href=url
    },
    loadBusiness(){
      this.$request.get('business/selectByUserId?id='+this.user.id).then(res=>{
        if(res.code == '200'){
          this.businessData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    cancelFavourites(id){
      this.$request.delete('collect/deleteByBusinessId/'+id).then(res=>{
        if(res.code === '200'){
          this.$message.success('取消成功')
        }else{
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>