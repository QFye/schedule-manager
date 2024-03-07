<template>
  <div class="main-content">
    <div style="width: 60%; display: flex;background-color: white; min-height: 1000px; margin:10px auto; border-radius: 20px; padding: 10px 0">
      <div style="flex: 1;padding: 0 20px;border-right: #cccccc 1px solid">
        <div style="font-size: 18px;height: 80px;line-height: 80px;border-bottom: #cccccc 1px solid">{{ eventCategoryData.name }}</div>
        <div style="padding: 10px 10px">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in eventData">
              <img @click="navTo('/front/eventDetail?id=' + item.id)" :src="item.img" style="width: 120px; height: 110px; border-radius: 10px">
              <div style="font-weight: 500; margin-top: 8px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                {{ item.name }}</div>
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; margin-top: 5px;color: red;font-weight: bold">￥{{ item.price }}</div>
            </el-col>
          </el-row>
        </div>
      </div>

      <div style="width:250px; margin-left: 20px;margin-right: 20px">
        <div style="font-size: 18px;height: 80px;line-height: 80px;border-bottom: #cccccc 1px solid">猜你喜欢</div>
        <div>猜不到</div>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    let eventCategoryId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      eventCategoryId: eventCategoryId,
      eventCategoryData: {},
      eventData: [],
    }
  },
  mounted() {
    this.loadEvent()
    this.loadEventCategory()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadEvent(){
      this.$request.get('event/selectByCategoryId?id='+this.eventCategoryId).then(res=>{
        if(res.code === '200'){
          this.eventData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadEventCategory(){
      this.$request.get('eventCategory/selectById?id='+this.eventCategoryId).then(res=>{
        if(res.code === '200'){
          this.eventCategoryData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url){
      location.href=url
    },
  }
}
</script>