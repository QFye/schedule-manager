<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入日程事件名称查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="事件名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="categoryName" label="事件类别" show-overflow-tooltip></el-table-column>
        <el-table-column prop="start" label="开始时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="last" label="持续时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="address" label="事件地点" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="事件状态" show-overflow-tooltip></el-table-column>
        <el-table-column prop="businessName" label="所属商家" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="所属用户" show-overflow-tooltip></el-table-column>
        <el-table-column prop="count" label="总销量" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="事件描述" show-overflow-tooltip></el-table-column>
        <el-table-column label="事件图标">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px;" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
          <el-table-column prop="price" label="价格" show-overflow-tooltip></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="name" label="事件名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="categoryId" label="事件类别">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option v-for="item in categoryData" :label="item.name" :value="item.id" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="start" label="开始时间">
          <el-time-picker
              v-model="form.start"
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
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="businessId" label="所属商家">
          <el-select v-model="form.businessId" placeholder="请选择所属商家">
            <el-option v-for="item in businessData" :label="item.name" :value="item.id" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="userId" label="所属用户">
          <el-select v-model="form.userId" placeholder="请选择所属用户">
            <el-option v-for="item in userData" :label="item.name" :value="item.id" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="price" label="价格">
          <el-input v-model="form.price" autocomplete="off"></el-input>
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
        <el-form-item label="事件描述" prop="description">
          <div id="editor" style="width:100%"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


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
  name: "Event",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        title: [
          {required: true, name: '请输入事件名称', trigger: 'blur'},
        ],
        content: [
          {required: true, img: '请上传事件图标', trigger: 'blur'},
        ]
      },
      ids: [],
      categoryData: [],
      businessData: [],
      userData: [],
      displayLast: new Date(2016, 9, 6, 0, 0),
    }
  },
  created() {
    this.load(1)
    this.loadCategory()
    this.loadBusiness()
    this.loadUser()
  },
  computed: {
    formattedLast(last) {
      // 将毫秒数转换为"HH:mm:ss"格式
      let totalSeconds = last / 1000;
      let hours = Math.floor(totalSeconds / 3600);
      let minutes = Math.floor((totalSeconds % 3600) / 60);
      let seconds = Math.floor(totalSeconds % 60);

      // 补零操作，确保时分秒两位显示
      hours = ("0" + hours).slice(-2);
      minutes = ("0" + minutes).slice(-2);
      seconds = ("0" + seconds).slice(-2);

      return `${hours}:${minutes}:${seconds}`;
    },
  },
  methods: {
    handleChange() {
      let value = this.displayLast.toLocaleTimeString()
      // 假设 value 是 "HH:mm:ss" 格式的时间字符串
      let timeArray = value.split(':');
      let hours = parseInt(timeArray[0]);
      let minutes = parseInt(timeArray[1]);
      let seconds = parseInt(timeArray[2]);

      // 更新 item.last 的值
      this.form.last = hours * 3600 + minutes * 60 + seconds;
    },
    loadUser(){
      this.$request.get('user/selectAll').then(res=>{
        if(res.code === '200'){
          this.userData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
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
    loadBusiness(){
      this.$request.get('business/selectAll').then(res=>{
        if(res.code === '200'){
          this.businessData = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      initWangEditor('')
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      initWangEditor(this.form.description || '')
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.description = editor.txt.html()
          this.$request({
            url: this.form.id ? '/event/update' : '/event/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/event/delete/' + id).then(res => {
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
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/event/delete/batch', {data: this.ids}).then(res => {
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
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleImageSuccess(response, file, fileList) {
      this.form.img = response.data
    },
  }
}
</script>

<style scoped>

</style>
