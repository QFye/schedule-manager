<template>
  <div class="main-content">
    <div style="height: 50px; background-color: rgba(97,148,255,0.91) "></div>
    <div style="display: flex">
      <div class="left"></div>
      <div style="width: 76%;background-color: rgba(246,252,255,0.91); margin: 15px 20px 15px 18px">
        <div style="height: 30px; color: rgba(61,109,204,0.91); margin: 13px 12px 5px 12px; font-weight: bold;font-size: 18px">日程市场</div>
        <div style="display: flex">
          <div style="width:17%; border-radius: 10px;background-color: rgba(246,252,255,0.91);padding-top: 3px">
            <div style="display: flex; margin: 14px 0px" v-for="item in categoryData">
              <img :src="item.img" style="width:20px; height:20px; margin: 0px 25px">
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><a href="#" @click="navTo('/front/eventCategory?id='+item.id)">{{ item.name }}</a></div>
            </div>
          </div>
          <div style="width:46%; height: 500px; margin-left: 5px">
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
          <div style="width:37%; height: 500px">
            <div style="display: flex; height: 30px; margin-top: 5px">
              <img src="@/assets/imgs/schedule_icon.png" style="height: 20px; width: 20px; margin-left: 30px" @click="navTo('/front/schedule')">
              <div style="margin-left: 15px"><a href="#" @click="navTo('/front/schedule')">编辑我的日程表>></a></div>
            </div>
            <ul class="infinite-list" v-infinite-scroll="load" style="overflow:auto;height: 380px; border-radius: 10px;background-color: rgba(246,252,255,0.91);margin-left: 10px;margin-right: 10px">
              <div style="margin-left: 25px; color: rgba(61,109,204,0.91); font-weight: bold;font-size: 16px;height: 30px; padding: 8px 5px 25px 0px">今日计划</div>
              <div style="display: flex;padding: 10px 10px 25px 25px; min-height: 15px; text-align: center" v-for="item in scheduleData">
                <div style="flex: 0.1">
                  <img src="@/assets/imgs/completed_icon.png" style="height: 20px; width: 20px">
                </div>
                <div style="flex: 0.4; text-align: center">{{item.start | extractTime}}~{{calculateEndTime(item)}}</div>
                <div style="flex: 0.5; text-align: center; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{item.name}}</div>
              </div>
            </ul>
            <div style="width:100%; height: 100px; display: flex; padding-left: 5px; margin-top: 22px">
              <div style="width:25% ;padding: 5px 15px 15px 15px; align-items: center; justify-content: center">
                <div style="text-align: center;">
                  <img src="@/assets/imgs/repository_icon.png" style="height: 20px; width: 20px; vertical-align: middle;">
                </div>
                <div style="margin: auto auto; font-size: 14px; text-align: center; text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><a href="#" @click="navTo('/front/repository')">日程仓库</a></div>
              </div>
              <div style="width:25% ;padding: 5px 15px 15px 15px">
                <div style="text-align: center;">
                  <img src="@/assets/imgs/team_icon.png" style="height: 20px; width: 20px">
                </div>
                <div style="margin: auto auto; font-size: 14px; text-align: center; text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><a href="#" @click="navTo('/front/teamHome')">我的团队</a></div>
              </div>
              <div style="width:25% ;padding: 5px 15px 15px 15px">
                <div style="text-align: center;">
                  <img src="@/assets/imgs/order_icon.png" style="height: 20px; width: 20px">
                </div>
                <div style="margin: auto auto; font-size: 14px; text-align: center; text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><a href="#" @click="navTo('/front/order')">我的钱包</a></div>
              </div>
              <div style="width:25% ;padding: 5px 15px 15px 15px">
                <div style="text-align: center;">
                  <img src="@/assets/imgs/favourites_icon.png" style="height: 20px; width: 20px">
                </div>
                <div style="margin: auto auto; font-size: 14px; text-align: center; text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><a href="#" @click="navTo('/front/collect')">我的收藏</a></div>
              </div>
            </div>
          </div>
        </div>

        <div style="margin: 20px 10px 10px 10px; font-weight: bold; color: rgb(255,255,255); font-size: 20px; height: 40px; background-color: #ff70a9; width: 110px; text-align: center;line-height: 40px; border-radius: 20px">热门计划</div>
        <div style="padding: 5px 5px 5px 5px">
          <el-row>
            <el-col :span="5" v-for="item in eventData" style="width: 20%">
              <div style="width: 100%; object-fit: contain; height: 200px; margin-bottom: 10px; background-color: #d2ddf1;border-radius: 10px">
                <a href="#" @click="navTo('/front/eventDetail?id=' + item.id)"><img :src="item.img" style="width: 100%; height: 100%; object-fit: contain;border-radius: 10px; margin: 0 auto"></a>
              </div>

              <div style="font-weight: 500; margin-top: 0; text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-weight: bold">
                {{ item.name }}</div>
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; margin-top: 5px;color: red;font-weight: bold" v-if="item.price !== 0">￥{{ item.price }}</div>
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; margin-top: 5px;color: red;font-weight: bold" v-else>免费</div>
            </el-col>
          </el-row>
        </div>

        <div style="margin: 20px 10px 10px 10px; font-weight: bold; color: rgb(255,255,255); font-size: 20px; height: 40px; background-color: #ff70a9; width: 110px; text-align: center;line-height: 40px; border-radius: 20px">为您推荐</div>
        <div style="padding: 5px 5px 5px 5px">
          <el-row>
            <el-col :span="5" v-for="item in recommendData" style="width: 20%">
              <div style="width: 100%; object-fit: contain; height: 200px; margin-bottom: 10px; background-color: #d2ddf1;border-radius: 10px">
                <a href="#" @click="navTo('/front/eventDetail?id=' + item.id)"><img :src="item.img" style="width: 100%; height: 100%; object-fit: contain;border-radius: 10px; margin: 0 auto"></a>
              </div>

              <div style="font-weight: 500; margin-top: 0; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;font-weight: bold">
                {{ item.name }}</div>
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; margin-top: 5px;color: red;font-weight: bold" v-if="item.price !== 0">￥{{ item.price }}</div>
              <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-size: 20px; margin-top: 5px;color: red;font-weight: bold" v-else>免费</div>
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
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      categoryData: [],
      scheduleData: [],
      carouselData1: [
          require('@/assets/imgs/ad1.jpg'),
          require('@/assets/imgs/ad2.jpg'),
          require('@/assets/imgs/ad3.jpg'),
      ],
      carouselData2: [
        require('@/assets/imgs/ad4.jpg'),
        require('@/assets/imgs/ad5.jpg'),
        require('@/assets/imgs/ad6.jpg'),
      ],
      carouselData3: [
        require('@/assets/imgs/ad7.jpg'),
        require('@/assets/imgs/ad8.jpg'),
        require('@/assets/imgs/ad9.jpg'),
      ],
      eventData: [],
      recommendData: [],
      currentDate: new Date().toLocaleDateString(),
    }
  },
  mounted() {
    this.loadCategory()
    this.loadSchedule()
    this.loadEvent()
    this.loadRecommend()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadRecommend(){
      this.$request.get('event/recommend').then(res=>{
        if(res.code == '200'){
          this.recommendData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    calculateEndTime(item) {
      let startDate = new Date(item.start);
      let endDate = new Date(startDate.getTime() + item.last * 1000);

      // 将结束时间转换为HH:MM:SS格式
      let hours = String(endDate.getHours()).padStart(2, '0');
      let minutes = String(endDate.getMinutes()).padStart(2, '0');
      let seconds = String(endDate.getSeconds()).padStart(2, '0');

      return `${hours}:${minutes}:${seconds}`;
    },
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
      this.$request.get('event/selectByUserAndDate?id='+this.user.id+'&date='+this.currentDate).then(res=>{
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