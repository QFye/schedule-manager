<template>
  <div class="main-content">
    <div style="background-color: white;padding: 15px 20px;height: 45px">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>我的计划表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="width: 60%; background-color: white; min-height: 200px; margin:10px auto; border-radius: 20px; padding: 20px 15px">
      <div style="height: 30px; line-height: 30px; text-align: center; display: flex">
        <div style="; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">选择日期：</div>
        <el-date-picker
            v-model="currentDate"
            type="date"
            placeholder="选择日期">
        </el-date-picker>
        <el-button style="margin-left: 10px" @click="loadSchedule">切换</el-button>
        <el-button type="primary" @click="addVisible = true">添加</el-button>
        <el-button type="primary" @click="handleGenerate" style="text-align: right">智能生成计划</el-button>
      </div>
      <el-dialog title="信息" :visible.sync="addVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" :model="addEvent" ref="formRef">
          <el-form-item prop="name" label="事件名称">
            <el-input v-model="addEvent.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="categoryId" label="事件类别">
            <el-select v-model="addEvent.categoryId" placeholder="请选择分类">
              <el-option v-for="item in categoryData" :label="item.name" :value="item.id" :key="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="start" label="开始时间">
            <el-time-picker
              v-model="addEvent.start"
              type="time"
              placeholder="选择时间">
            </el-time-picker>
          </el-form-item>
          <el-form-item prop="last" label="持续时间">
            <el-time-picker
                v-model="displayLast"
                @change="handleChange"
                type="time"
                placeholder="选择时间">
            </el-time-picker>
          </el-form-item>
          <el-form-item prop="address" label="事件地点">
            <el-input v-model="addEvent.address" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="description" label="事件描述">
            <el-input v-model="addEvent.description" autocomplete="off"></el-input>
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
          <el-button @click="addVisible = false">取 消</el-button>
          <el-button type="primary" @click="save(addEvent)">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="生成计划" :visible.sync="generateVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="计划事件" name="计划事件">
            <template>
              <ul class="infinite-list" v-infinite-scroll="load" style="overflow:auto">
                <li v-for="(item, index) in generateData" class="infinite-list-item">
                  <div style="display: flex; margin: 10px 0; padding: 5px 30px">
                    <div style="width: 20%; text-align: center;">
                      <div style="border: #cccccc 1px solid; border-radius: 10px; width: 50%; height: 50px">
                        <img :src="item.img" style="width: 100%; height: 50px">
                      </div>
                    </div>
                    <div style="width: 60%">
                      <div>
                        {{item.name}}
                      </div>
                      <div>
                        {{item.start | extractTime}}
                      </div>
                    </div>
                    <div style="width:20%; text-align: right">
                      <el-button type="danger" icon="el-icon-delete" circle @click="removeFromGenerate(item)"></el-button>
                    </div>
                  </div>
                </li>
              </ul>
            </template>
          </el-collapse-item>
          <el-collapse-item title="属性" name="属性">
            <div v-for="item in historyData" style="margin: 2px 0; padding: 5px 30px">
              {{item.name}} : {{item.value}}
            </div>
          </el-collapse-item>
        </el-collapse>

        <div slot="footer" class="dialog-footer">
          <el-button @click="generateVisible = false">取 消</el-button>
          <el-button type="primary" @click="HandleAddGenerate">添 加</el-button>
        </div>
      </el-dialog>

      <div style="margin-top: 20px; padding: 10px 10px 10px 10px">
        <h3 style="margin-bottom: 8px">当前日期：{{ showDate.toLocaleDateString() }}</h3>
        <div v-if="scheduleData.length === 0" style="margin-top: 40px">计划表空空如也哦，快来新建事件吧！</div>
        <el-timeline v-else>
          <el-timeline-item :timestamp="item.start | extractTime" placement="top" v-for="(item, index) in scheduleData" :key="item.id">
            <el-card>
              <div style="display: flex">
                <div style="width: 12%;text-align: center">
                  <img :src="item.img" style="width: 80%; height: 72px; border: #cccccc 2px solid;background-color: #c8d6f1">
                </div>
                <div style="width: 30%;border-right: #cccccc 2px solid;padding-left: 16px">
                    <h3 style="margin-top: 5px; margin-bottom: 2.5px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{ item.name }}</h3>
                  <p style="margin-top: 2.5px; margin-bottom: 2.5px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">地点：{{item.address}}</p>
                  <p style="margin-top: 2.5px; margin-bottom: 5px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">类别：{{item.categoryName}}</p>

                  <el-dialog title="删除" :visible.sync="item.deleteConfirmVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                    <div style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                      事件 {{item.name}} 删除后将无法恢复，是否确认删除？
                    </div>
                    <div slot="footer" class="dialog-footer">
                      <el-button @click="hideDeleteBar(index)" style="height: 50%">取消</el-button>
                      <el-button type="primary" @click="del(item)" style="height: 50%">确认</el-button>
                    </div>
                  </el-dialog>
                  <el-dialog title="信息" :visible.sync="item.editVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
                    <el-form label-width="100px" style="padding-right: 50px" :model="item" ref="formRef">
                      <el-form-item prop="name" label="事件名称">
                        <el-input v-model="item.name" autocomplete="off"></el-input>
                      </el-form-item>
                      <el-form-item prop="start" label="开始时间">
                        <el-time-picker
                            v-model="item.start"
                            type="time"
                            placeholder="选择时间">
                        </el-time-picker>
                      </el-form-item>
                      <el-form-item prop="address" label="事件地点">
                        <el-input v-model="item.address" autocomplete="off"></el-input>
                      </el-form-item>
                      <el-form-item prop="description" label="事件描述">
                        <el-input v-model="item.description" autocomplete="off"></el-input>
                      </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                      <el-button @click="hideEditBar(index)">取 消</el-button>
                      <el-button type="primary" @click="save(item)">确 定</el-button>
                    </div>
                  </el-dialog>
                </div>
                <div style="width: 48%; padding-left: 16px">
                  <div style="padding-top: 10px;padding-bottom: 10px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                    简介：
                    <div v-html="item.description"></div>
                  </div>
                </div>
                <div style="margin-top: 10px; margin-bottom: 5px; width: 10%" v-if="item.status === 'PERSONAL'">
                  <div style="margin-bottom: 8px;text-align: center">
                    <el-button type="primary" icon="el-icon-edit" style="width: 70%" @click="showEditBar(index)"></el-button>
                  </div>
                  <div style="text-align: center">
                    <el-button type="primary" icon="el-icon-delete" style="width:70%" @click="showDeleteBar(index)"></el-button>
                  </div>
                </div>

                <div style="margin-top: 10px; margin-bottom: 5px; width: 10%" v-if="item.status === 'UNCONFIRMED'">
                  <div style="margin-bottom: 8px;text-align: center">
                    <el-button type="primary" style="width: 70%" @click="accept(item)">接受</el-button>
                  </div>
                  <div style="text-align: center">
                    <el-button type="primary" style="width:70%" @click="refuse(item)">取消</el-button>
                  </div>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user')||'{}'),
      scheduleData: [],
      generateData: [],
      currentDate: new Date(),
      showDate: this.currentDate,
      addEvent: {},
      addVisible: false,
      generateVisible: false,
      categoryData: [],
      historyData: [],
      displayLast: new Date(2016, 9, 0, 0, 0),
      activeNames: ['计划事件']
    }
  },
  mounted() {
    this.loadSchedule()
    this.loadEvent()
    this.loadCategory()
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
    removeFromGenerate(item) {
      const index = this.generateData.indexOf(item);
      if (index > -1) {
        this.generateData.splice(index, 1);
      }
    },
    HandleAddGenerate(){
      this.generateData.forEach((event) => {
        let initialId = event.id
        event.status = 'PERSONAL'
        event.id = null
        this.$request({
          url: '/event/applyInSchedule?userId='+this.user.id+'&date='+this.showDate.toLocaleDateString()+'&initialId='+initialId,
          method: 'POST',
          data: event
        }).then(res => {
          if (res.code === '200') {  // 表示成功保存
            this.load(1)
            this.fromVisible = false
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      })
      this.$message.success('添加成功')
      this.generateVisible = false
    },
    handleChange() {
      let value = this.displayLast.toLocaleTimeString()
      // 假设 value 是 "HH:mm:ss" 格式的时间字符串
      let timeArray = value.split(':');
      let hours = parseInt(timeArray[0]);
      let minutes = parseInt(timeArray[1]);
      let seconds = parseInt(timeArray[2]);

      // 更新 item.last 的值
      this.addEvent.last = hours * 3600 + minutes * 60 + seconds;
    },
    showDeleteBar(index){
      this.$set(this.scheduleData[index], 'deleteConfirmVisible', true)
    },
    showEditBar(index){
      this.$set(this.scheduleData[index], 'editVisible', true)
    },
    hideDeleteBar(index){
      this.$set(this.scheduleData[index], 'deleteConfirmVisible', false)
    },
    hideEditBar(index){
      this.$set(this.scheduleData[index], 'editVisible', false)
    },
    loadCategory(){
      this.$request.get('eventCategory/selectAll').then(res=>{
        if(res.code === '200'){
          this.categoryData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadGenerate(){
      this.$request.get('event/generateSchedule/'+this.user.id+'?date='+this.showDate.toLocaleDateString()).then(res=>{
        if(res.code === '200'){
          this.generateData = res.data
          this.$message.success("生成成功");
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadHistory(){
      this.$request.get('event/getHistory').then(res=>{
        if(res.code === '200'){
          this.historyData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleGenerate(){
      this.loadGenerate()
      this.loadHistory()
      this.generateVisible = true
    },
    accept(item){
      item.status = "PERSONAL"
      this.$request({
        url: '/event/update',
        method:'PUT',
        data: item
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    refuse(item){
      this.$confirm('您确定不接受当前团队事件吗？', '确认', {type: "warning"}).then(response => {
        this.$request.delete('/event/deleteInSchedule/' + item.id+'?userId='+this.user.id+'&date='+this.showDate.toLocaleDateString()).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    del(item) {   // 单个删除
      item.deleteConfirmVisible = false
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/event/deleteInSchedule/' + item.id+'?userId='+this.user.id+'&date='+this.showDate.toLocaleDateString()).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
      this.loadSchedule()
    },
    loadEvent(){
      this.addEvent.status = 'PERSONAL'
    },
    save(item) {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.addVisible = false
      item.editVisible = false
      this.$request({
        url: item.id ? '/event/update' : '/event/addInSchedule?userId='+this.user.id+'&date='+this.showDate.toLocaleDateString(),
        method: item.id ? 'PUT' : 'POST',
        data: item
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
      this.loadSchedule()
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
      this.addEvent.img = response.data
    },
    navTo(url){
      location.href=url
    },
    toggleToToday() {
      // 更新currentDate为当前日期并格式化
      this.currentDate = new Date().toLocaleDateString();
    },
    loadSchedule(){
      this.showDate = this.currentDate
      this.$request.get('event/selectByUserAndDate?id='+this.user.id+'&date='+this.currentDate.toLocaleDateString()).then(res=>{
        if(res.code == '200'){
          this.scheduleData = res.data
          this.scheduleData.editVisible = false
          this.scheduleData.deleteConfirmVisible = false
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
  }
}
</script>