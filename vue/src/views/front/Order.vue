<template>
  <div class="main-content">
    <div style="width: 60%; display: flex;background-color: rgba(246,252,255,0.91); min-height: 200px; margin:10px auto; border-radius: 20px; padding: 40px 50px">
      <div>
        <img :src="user.avatar" style="width: 120px; height: 120px; border-radius: 60px; margin-right: 30px">
      </div>
      <div style="flex:1">
        <div style="text-align: center; font-size: 30px; font-weight: bold; margin-bottom: 12px">{{user.name}} 的钱包</div>
        <div style="text-align: center; font-size: 25px; color: red;font-weight: bold; margin-bottom: 12px">余额：￥ {{user.money}} </div>
        <div style="text-align: center; font-size: 25px; color: white;font-weight: bold;background-color: #7fa0df;border-radius: 10px;width:22%;height: 34px;line-height: 34px;margin:0 auto">立即充值</div>
      </div>
      <div>
        <img src="@/assets/imgs/钱包.png" style="width:120px;height: 120px;border-radius: 10px">
      </div>
    </div>
    <div style="width: 60%;background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <div style="font-size: 25px; color: #7fa0df;text-align: center;font-weight: bold;border-radius: 10px;width:15%;height: 34px;line-height: 34px;margin:0 10px;border-bottom: #cccccc 1px solid">我的账单</div>
      <div v-for="item in orderData" style="padding: 20px 18px; border-bottom: #cccccc 1px solid">
        <div>
          订单号：{{item.id}}
        </div>
        <div>
          用户：{{item.user}}
        </div>
        <div>
          商品名称：{{item.event}}
        </div>
        <div>
          商家：{{item.business}}
        </div>
        <div>
          商家联系电话：{{item.phone}}
        </div>
        <div>
          交易时间：{{item.date}}
        </div>
        <div>
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
  }
}
</script>