<template>
  <div class="main-content">
    <div style="width: 60%; display: flex;background-color: rgba(246,252,255,0.91); min-height: 200px; margin:10px auto; border-radius: 20px; padding: 40px 50px">
      <div style="width: 20%">
        <img :src="user.avatar" style="width: 80%; height: 120px; border-radius: 60px; margin-right: 30px">
      </div>
      <div style="width: 60%">
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;text-align: center; font-size: 30px; font-weight: bold; margin-bottom: 12px">{{user.name}} 的钱包</div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;text-align: center; font-size: 25px; color: red;font-weight: bold; margin-bottom: 12px">余额：￥ {{user.money}} </div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;text-align: center; font-size: 25px; color: white;font-weight: bold;background-color: #7fa0df;border-radius: 10px;width:35%;height: 34px;line-height: 34px;margin:0 auto" @click="HandleClick">立即充值</div>
      </div>
      <div style="width: 20%">
        <img src="@/assets/imgs/钱包.png" style="width:80%;height: 120px;border-radius: 10px">
      </div>
    </div>
    <div style="width: 60%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;font-size: 25px; color: #7fa0df;text-align: center;font-weight: bold;border-radius: 10px;width:15%;height: 34px;line-height: 34px;margin:0 10px;border-bottom: #cccccc 1px solid">我的账单</div>
      <div v-for="item in orderData" style="padding: 20px 18px; border-bottom: #cccccc 1px solid">
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
          订单号：{{item.id}}
        </div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
          用户：{{item.user}}
        </div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
          商品名称：{{item.event}}
        </div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
          商家：{{item.business}}
        </div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
          商家联系电话：{{item.phone}}
        </div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
          交易时间：{{item.date | extractDate}} {{item.date | extractTime}}
        </div>
        <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
          详情信息：{{item.details}}
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
      orderData: [],
    }
  },
  mounted() {
    this.loadOrder()
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
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    navTo(url){
      location.href=url
    },
    loadOrder(){
      this.$request.get('order/selectByUserId?id='+this.user.id).then(res=>{
        if(res.code == '200'){
          this.orderData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    HandleClick(){
      this.user.money += 50
      let jsonUser = JSON.stringify(this.user);
      localStorage.setItem('xm-user', jsonUser);
      this.save()
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$request({
            url: this.user.id ? '/user/update' : '/user/add',
            method: this.user.id ? 'PUT' : 'POST',
            data: this.user
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
        })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/event/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
  }
}
</script>