<template>
  <div class="main-content">
    <div style="background-color: white;padding: 15px 20px;height: 45px">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/front/eventCategory?id='+eventData.categoryId }">{{eventData.categoryName}}</el-breadcrumb-item>
        <el-breadcrumb-item>{{eventData.name}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="width: 60%; background-color: white; min-height: 1000px; margin:10px auto; border-radius: 20px">
      <div>
        <el-row :gutter="20">
          <el-col :span="12">
            <img :src="eventData.img" style="height:280px;width: 90%;margin: 50px 50px 0px 50px">
          </el-col>
          <el-col :span="12" style="padding: 70px 50px">
            <div style="text-align: center; font-weight: bold; font-size: 25px; height: 30px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{eventData.name}}</div>
            <div style="display: flex">
              <div style="text-align: center; font-size: 18px;background-color: #bbcffc; height: 30px; width: 60%; margin: 10px auto;border-radius: 15px; display: flex; padding-left: 5px">
                <div style="width: 30px; height: 30px; margin: auto 10px">
                  <img :src="eventData.categoryImg" style="height: 25px; width: 25px; margin: 2.5px 0">
                </div>
                <div style="margin: auto 5px; text-align: center; text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><a href="#" @click="navTo('/front/eventCategory?id='+eventData.categoryId)">{{eventData.categoryName}}</a></div>
              </div>
              <div style="text-align: center; font-size: 18px; height: 30px;margin: auto auto; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">总销量：{{eventData.count}}</div>
            </div>
            <div style="padding-left: 20px">
              <div style="font-size: 18px; height: 20px;line-height: 20px;margin: 0 auto; text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><a href="#" @click="navTo('/front/business?id='+eventData.businessId)">商家：{{eventData.businessName}}</a></div>
              <div style="font-size: 18px; height: 20px;line-height: 20px;margin: 10px auto; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">时间段：{{eventData.start | extractTime}}~{{calculateEndTime(eventData)}}</div>
              <div style="font-size: 18px; height: 20px;line-height: 20px;margin: 10px auto; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">事件地点：{{eventData.address}}</div>
              <div style="font-size: 18px; height: 20px;line-height: 20px;margin: 10px 0px 20px 0px;color: red; font-weight: bold; text-overflow: ellipsis; overflow: hidden; white-space: nowrap" v-if="eventData.price !== 0">计划售价：{{eventData.price}}/元</div>
              <div style="font-size: 20px; height: 20px;line-height: 20px;margin: 40px 20px 20px 20px;color: red; font-weight: bold; text-overflow: ellipsis; overflow: hidden; white-space: nowrap" v-else>免费</div>
            </div>
            <div style="display: flex; margin: 5px 0px 5px 0px">
              <div style="width: 50%; margin: 0 auto; text-align: center">
                <el-button  type="primary" @click="buyDialogVisible = true">加入计划表</el-button>
                <el-dialog title="信息" :visible.sync="dialogVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                  <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
                    <el-form-item prop="name" label="事件名称">
                      <el-input v-model="form.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="name" label="开始时间">
                      <el-input v-model="form.start" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="name" label="持续时间" disabled="true">
                      <el-input v-model="form.last" autocomplete="off" disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item prop="address" label="事件地点">
                      <el-input v-model="form.address" autocomplete="off" disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item prop="description" label="事件描述">
                      <el-input v-model="form.description" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="price" label="价格">
                      <el-input v-model="form.price" autocomplete="off" disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="事件图标">
                      <el-upload
                          class="avatar-uploader"
                          :action="$baseUrl + '/files/upload'"
                          :headers="{ token: user.token }"
                          list-type="picture"
                          :on-success="handleImageSuccess"
                      >
                        <el-button type="primary">上传图标</el-button>
                      </el-upload>
                    </el-form-item>
                  </el-form>
                  <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="save">确 定</el-button>
                  </div>
                </el-dialog>

                <el-dialog title="信息" :visible.sync="buyDialogVisible" width="20%" :close-on-click-modal="false" destroy-on-close>
                  <div>
                    需支付：{{eventData.price}}
                  </div>
                  <div>
                    账户余额：{{user.money}}
                  </div>
                  <div>
                    是否继续？
                  </div>
                  <div slot="footer" class="dialog-footer">
                    <el-button @click="buyDialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="HandlePurchase">确 定</el-button>
                  </div>
                </el-dialog>

                <el-dialog title="信息" :visible.sync="unaffordable" width="20%" :close-on-click-modal="false" destroy-on-close>
                  <div>
                    余额不足，请充值后重试。
                  </div>
                  <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="unaffordable = false">确 定</el-button>
                  </div>
                </el-dialog>

              </div>
              <div style="width: 50%; text-align: center; margin: 0 auto" v-if="alreadyPurchased===false">
                <el-button type="primary" @click="saveRepository">添加入库</el-button>
              </div>
              <div style="width: 50%; text-align: center; margin: 0 auto" v-else>
                <el-button disabled type="primary">已添加</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <div style="padding: 5px 30px">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="计划详情" name="details" style="padding: 20px 30px">
            <div v-html="eventData.description" style="padding: 10px 20px"></div>
          </el-tab-pane>
          <el-tab-pane label="评论" name="comments" style="padding: 10px 20px">
            <div v-if="commentData.length === 0">
              本计划暂无评论哦~
            </div>
            <div v-for="item in commentData">
              <div style="display: flex; margin-top: 10px">
                <div>
                  <img :src="item.avatar" style="height: 60px; width: 60px; border-radius: 30px" alt="">
                </div>
                <div style="margin-left: 20px">
                  <div style="font-size: 20px;font-weight: bold; margin-bottom: 5px">{{ item.name }}</div>
                  <div>{{ item.time | extractDate }} {{item.time | extractTime}}</div>
                </div>
              </div>
              <div style="margin-top: 15px; min-height: 20px; font-size: 18px; margin-left: 5px; padding-bottom: 20px;margin-bottom: 5px;border-bottom: #cccccc 1px solid" v-html="item.commentContent"></div>
            </div>
            <div style="margin-top: 50px">
              <div style="margin-bottom: 10px;font-weight: bold;font-size: 20px; margin-left: 5px">
                发表评论
              </div>
              <el-form label-width="0px" :model="newComment">
                <el-form-item prop="commentContent">
                  <div id="editor" style="width:100%"></div>
                </el-form-item>
              </el-form>
            </div>
            <div style="margin-top: 10px; text-align: right">
              <el-button type="primary" @click="HandleComment">发表</el-button>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import E from 'wangeditor'

let editor
function initWangEditor(content){
  setTimeout(()=>{
    if(!editor){
      editor = new E("#editor")
      editor.config.placeholder = "请输入内容"
      editor.config.uploadFileName = "file"
      editor.config.uploadImgServer = "http://localhost:9090/files/wang/upload"
      editor.create()
    }
    editor.txt.html(content)
  }, 0)
}
export default {

  data() {
    let eventId = this.$route.query.id
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      eventId: eventId,
      eventData: {},
      activeName: 'details',
      commentData: [],
      dialogVisible: false,
      buyDialogVisible: false,
      unaffordable: false,
      alreadyPurchased: false,
      form: {},
      initial:{},
      order:{},
      newComment:{},
      currentDate:new Date(),
      rules: {
        title: [
          {required: true, name: '请输入事件名称', trigger: 'blur'},
        ],
        content: [
          {required: true, img: '请上传事件图标', trigger: 'blur'},
        ]
      },
    }
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
  mounted() {
    this.loadEvent()
    this.loadComment()
    this.loadForm()
    this.searchRepository()
    initWangEditor('')
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    HandleComment(){
      this.newComment.commentContent = editor.txt.html()
      this.newComment.eventId = this.eventData.id
      this.newComment.userId = this.user.id
      this.$request({
        url: '/comment/add',
        method: 'POST',
        data: this.newComment
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success("保存成功")
          this.load(1)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
      this.loadComment()
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
    searchRepository(){
      this.$request.get('event/selectFromRepository?userId='+this.user.id+'&eventId='+this.eventId).then(res=>{
        if(res.code === '200'){
          if(res.data === null){
            this.alreadyPurchased = false
          } else {
            this.alreadyPurchased = true
          }
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    HandlePurchase(){
      this.buyDialogVisible = false
      if(this.eventData.price <= this.user.money){
        this.dialogVisible = true
        this.initial.count++
      }else{
        this.unaffordable = true
      }
    },
    loadForm(){
      this.$request.get('event/selectById?id='+this.eventId).then(res=>{
        if(res.code == '200'){
          this.form = res.data
          this.initial = res.data
        }else{
          this.$message.error(res.msg)
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
    handleImageSuccess(response, file, fileList) {
      this.form.img = response.data
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    saveRepository(){
      this.$request({
        url: '/event/addRepositoryOnly?userId='+this.user.id,
        method: 'POST',
        data: this.eventData
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success("保存成功")
          this.load(1)
          this.alreadyPurchased = true
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      if(this.dialogVisible === true) {
        this.dialogVisible = false
      }
      this.$request({
        url: this.initial.id ? '/event/update' : '/event/add',
        method: this.initial.id ? 'PUT' : 'POST',
        data: this.initial
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })

      this.form.id = null
      this.form.status = "PERSONAL"
      this.form.count = 0
      this.$request({
        url: this.form.id ? '/event/update' : '/event/addInSchedule?userId='+this.user.id+'&date='+this.currentDate.toLocaleDateString(),
        method: this.form.id ? 'PUT' : 'POST',
        data: this.form
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })

      this.user.money -= this.eventData.price
      let jsonUser = JSON.stringify(this.user);
      localStorage.setItem('xm-user', jsonUser);
      this.$refs.formRef.validate((valid) => {
        if (valid) {
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
        }
      })

      this.order.price = this.eventData.price
      this.order.userId = this.user.id
      this.order.eventId = this.eventData.id
      this.order.details = '无'
      this.$request({
        url: this.order.id ? '/order/update' : '/order/add',
        method: this.order.id ? 'PUT' : 'POST',
        data: this.order
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success("保存成功")
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
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