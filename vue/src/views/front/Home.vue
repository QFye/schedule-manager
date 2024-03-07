<template>
  <div class="main-content">
    <div style="height: 50px; background-color: rgba(97,148,255,0.91) "></div>
    <div style="display: flex">
      <div class="left"></div>
      <div style="width: 76%;background-color: rgba(246,252,255,0.91); margin: 15px 20px 15px 18px">
        <div style="height: 30px; color: rgba(61,109,204,0.91); margin: 13px 12px 5px 12px; font-weight: bold;font-size: 18px">日程市场</div>
        <div style="display: flex">
          <div style="flex: 2; border-radius: 10px;background-color: rgba(246,252,255,0.91)">
            <div style="display: flex; margin: 14px 0px" v-for="item in categoryData">
              <img :src="item.img" style="width:20px; height:20px; margin: 0px 25px">
              <div><a href="#" @click="navTo('/front/eventCategory?id='+item.id)">{{ item.name }}</a></div>
            </div>
          </div>
          <div style="flex: 5; height: 500px; margin-left: 5px">
            <div style="margin-bottom:10px;">
              <el-carousel height="290px" style="border-radius: 10px">
                <el-carousel-item v-for="item in carouselData1">
                  <img :src="item" style="width:100%; height: 300px; border-radius: 10px">
                </el-carousel-item>
              </el-carousel>
            </div>
            <div style="display: flex">
              <div style="flex: 5;margin-right: 2.5px;border-radius: 10px">
                <el-carousel height="200px" style="border-radius: 10px">
                  <el-carousel-item v-for="item in carouselData2">
                    <img :src="item" style="width:100%; height: 200px; border-radius: 10px">
                  </el-carousel-item>
                </el-carousel>
              </div>
              <div style="flex: 5;margin-left: 2.5px;border-radius: 10px">
                <el-carousel height="200px" style="border-radius: 10px">
                  <el-carousel-item v-for="item in carouselData3">
                    <img :src="item" style="width:100%; height: 200px; border-radius: 10px">
                  </el-carousel-item>
                </el-carousel>
              </div>
            </div>
          </div>
          <div style="flex: 4; height: 500px">
            <div style="display: flex; height: 30px; margin-top: 5px">
              <img src="@/assets/imgs/schedule_icon.png" style="height: 20px; width: 20px; margin-left: 30px">
              <div style="margin-left: 15px">编辑我的日程表>></div>
            </div>
            <div style="height: 380px; border-radius: 10px;background-color: rgba(246,252,255,0.91);margin-left: 10px;margin-right: 10px">
              <div style="margin-left: 25px; color: rgba(61,109,204,0.91); font-weight: bold;height: 30px; padding: 8px 5px 0px 0px">今日计划</div>
              <div style="display: flex;padding: 10px 10px 25px 25px" v-for="item in scheduleData">
                <div style="flex: 2">
                  <img src="@/assets/imgs/completed_icon.png" style="height: 20px; width: 20px">
                </div>
                <div style="flex: 3; text-align: center">{{item.start}}</div>
                <div style="flex: 4;margin: 0 0 15px 5px; text-align: center">{{item.name}}</div>
                <div style="flex: 2.5">编辑事件</div>
              </div>
            </div>
            <div style="height: 100px; display: flex; padding-left: 5px; margin-top: 18px">
              <div style="padding: 5px 15px 15px 15px">
                <img src="@/assets/imgs/repository_icon.png" style="height: 20px; width: 20px; margin-left: 30px">
                <div style="margin-left: 10px">日程仓库</div>
              </div>
              <div style="padding: 5px 15px 15px 15px">
                <img src="@/assets/imgs/team_icon.png" style="height: 20px; width: 20px; margin-left: 30px">
                <div style="margin-left: 10px">我的团队</div>
              </div>
              <div style="padding: 5px 15px 15px 15px">
                <img src="@/assets/imgs/order_icon.png" style="height: 20px; width: 20px; margin-left: 30px">
                <div style="margin-left: 10px">我的订单</div>
              </div>
              <div style="padding: 5px 15px 15px 15px">
                <img src="@/assets/imgs/favourites_icon.png" style="height: 20px; width: 20px; margin-left: 30px">
                <div style="margin-left: 10px">我的收藏</div>
              </div>
            </div>
          </div>
        </div>

        <div style="margin: 20px 10px 10px 10px; font-weight: bold; color: rgb(255,255,255); font-size: 20px; height: 40px; background-color: #ff70a9; width: 110px; text-align: center;line-height: 40px; border-radius: 20px">热门计划</div>
        <div style="padding: 0 0 0px 5px">
          <el-row>
            <el-col :span="5" v-for="item in eventData">
              <img @click="navTo('/front/eventDetail?id=' + item.id)" :src="item.img" style="width: 200px; height: 190px; border-radius: 10px">
              <div style="font-weight: 500; margin-top: 8px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                {{ item.name }}</div>
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; margin-top: 5px;color: red;font-weight: bold">￥{{ item.price }}</div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="right"></div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      categoryData: [],
      scheduleData: [],
      carouselData1: [
          require('@/assets/imgs/bg.jpg'),
          require('@/assets/imgs/bg1.jpg'),
          require('@/assets/imgs/logo.png'),
      ],
      carouselData2: [
        require('@/assets/imgs/bg.jpg'),
        require('@/assets/imgs/bg1.jpg'),
        require('@/assets/imgs/logo.png'),
      ],
      carouselData3: [
        require('@/assets/imgs/bg.jpg'),
        require('@/assets/imgs/bg1.jpg'),
        require('@/assets/imgs/logo.png'),
      ],
      eventData: [],
    }
  },
  mounted() {
    this.loadCategory()
    this.loadSchedule()
    this.loadEvent()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadCategory(){
      this.$request.get('eventCategory/selectAll').then(res=>{
        if(res.code == '200'){
          this.categoryData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadSchedule(){
      this.$request.get('event/selectAll').then(res=>{
        if(res.code == '200'){
          this.scheduleData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadEvent(){
      this.$request.get('event/selectTop10').then(res=>{
        if(res.code == '200'){
          this.eventData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url){
      location.href=url
    }
  }
}
</script>

<style scoped>
.main-content{
  min-height: 100px;
  background-size: 100%;
  background-color: rgba(97,148,255,0.91);
}

.left{
  width: 12%;
  background-repeat: no-repeat;
  background-color: rgba(97,148,255,0.91);
}

.right{
  width: 12%;
  background-repeat: no-repeat;
  background-color: rgba(97,148,255,0.91);
}

.el-col-5{
  width: 20%;
  max-width: 20%;
  padding: 10px 10px;
}
</style>