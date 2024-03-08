<template>
  <div class="main-content">
    <div style="width: 60%; background-color: white; min-height: 1000px; margin:10px auto; border-radius: 20px">
      <div>
        <el-row :gutter="20">
          <el-col :span="12">
            <img :src="eventData.img" style="height:280px;width: 90%;margin: 50px 50px 0px 50px">
          </el-col>
          <el-col :span="12" style="padding: 70px 50px">
            <div style="text-align: center; font-weight: bold; font-size: 25px; height: 30px">{{eventData.name}}</div>
            <div style="display: flex">
              <div style="text-align: center; font-size: 18px;background-color: #bbcffc; height: 30px; width: 60%; margin: 10px auto;border-radius: 15px; display: flex; padding-left: 5px">
                <div style="width: 30px; height: 30px; margin: auto 10px">
                  <img :src="eventData.categoryImg" style="height: 25px; width: 25px; margin: 2.5px 0">
                </div>
                <div style="margin: auto 5px; text-align: center"><a href="#" @click="navTo('/front/eventCategory?id='+eventData.categoryId)">{{eventData.categoryName}}</a></div>
              </div>
              <div style="text-align: center; font-size: 18px; height: 30px;margin: auto auto">总销量：{{eventData.count}}</div>
            </div>
            <div style="text-align: center; font-size: 18px; height: 20px;margin: 0 auto"><a href="#" @click="navTo('/front/business?id='+eventData.businessId)">商家：{{eventData.businessName}}</a></div>
            <div style="text-align: center; font-size: 18px; height: 20px;margin: 10px auto">时间段：{{eventData.start}}</div>
            <div style="text-align: center; font-size: 18px; height: 20px;margin: 10px auto">事件地点：{{eventData.address}}</div>
            <div style="text-align: center; font-size: 20px; height: 20px;margin: 10px 0px 20px 0px;color: red; font-weight: bold" v-if="eventData.price !== 0">计划售价：{{eventData.price}}/元</div>
            <div style="text-align: center; font-size: 20px; height: 20px;margin: 40px 20px 20px 20px;color: red; font-weight: bold" v-else>免费</div>
            <div style="display: flex; margin: 5px 0px 5px 40px">
              <div style="flex: 1; margin: 0 auto">
                <div style="text-align: center;  color:white; background-color: rgba(61,109,204,0.91); width: 60%; height: 30px;line-height: 30px;border-radius: 15px">加入计划表</div>
              </div>
              <div style="flex: 1; margin: 0 auto">
                <div style="flex: 1; color:white; text-align: center; background-color: rgba(61,109,204,0.91); width: 60%; height: 30px;line-height: 30px;border-radius: 15px">收藏</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <div style="padding: 5px 30px">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="计划详情" name="details">
            <div v-html="eventData.description" style="padding: 10px 20px"></div>
          </el-tab-pane>
          <el-tab-pane label="评论" name="comments" style="padding: 10px 20px">
            <div v-for="item in commentData">
              <div style="display: flex; margin-top: 10px">
                <div>
                  <img :src="item.avatar" style="height: 60px; width: 60px; border-radius: 30px" alt="">
                </div>
                <div style="margin-left: 20px">
                  <div style="font-size: 20px;font-weight: bold; margin-bottom: 5px">{{ item.name }}</div>
                  <div>{{ item.time }}</div>
                </div>
              </div>
              <div style="margin-top: 15px; min-height: 20px; font-size: 18px; margin-left: 5px; padding-bottom: 20px;margin-bottom: 5px;border-bottom: #cccccc 1px solid">
                {{ item.commentContent }}
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
    let eventId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      eventId: eventId,
      eventData: {},
      activeName: 'details',
      commentData: [],
    }
  },
  mounted() {
    this.loadEvent()
    this.loadComment()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadEvent(){
      this.$request.get('event/selectById?id='+this.eventId).then(res=>{
        if(res.code == '200'){
          this.eventData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadComment(){
      this.$request.get('comment/selectByEventId?id='+this.eventId).then(res=>{
        if(res.code == '200'){
          this.commentData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleClick(tab, event){

    },
    navTo(url){
      location.href=url
    }
  }
}
</script>