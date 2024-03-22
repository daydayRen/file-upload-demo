<template>
  <el-form ref="form" :model="form" label-width="110px">
    <el-form-item label="文件ID">
      <el-input v-model="form.id" type="number" placeholder="请输入数字"></el-input>
    </el-form-item>
    <el-button style="margin-left: 10px; margin-bottom: 15px" type="primary" @click="getDetail">查询</el-button>
    <el-button style="margin-left: 10px; margin-bottom: 15px" type="warning" @click="clearDetail">清空表单</el-button>
  </el-form>

  <el-form ref="form" :model="form" label-width="110px">
    <el-form-item label="存储文件名">
      <el-input v-model="form.storageName"></el-input>
    </el-form-item>
    <el-form-item label="文件大小">
      <el-input v-model="form.fileSize"></el-input>
    </el-form-item>
    <el-form-item label="文件类型">
      <el-input v-model="form.fileType"></el-input>
    </el-form-item>
    <el-form-item label="文件后缀">
      <el-input v-model="form.fileSuffix"></el-input>
    </el-form-item>
    <el-form-item label="子域code">
      <el-input v-model="form.subdomainCode"></el-input>
    </el-form-item>
    <el-form-item label="文件业务code">
      <el-input v-model="form.businessCode"></el-input>
    </el-form-item>
    <el-form-item label="桶名称">
      <el-input v-model="form.bucketName"></el-input>
    </el-form-item>
    <el-form-item label="桶路径">
      <el-input v-model="form.bucketDomain"></el-input>
    </el-form-item>
    <el-form-item label="文件存储目录">
      <el-input v-model="form.fileDirectory"></el-input>
    </el-form-item>
    <el-form-item label="文件叶子目录">
      <el-input v-model="form.fileLeafDirectory"></el-input>
    </el-form-item>
    <el-form-item label="业务备注">
      <el-input v-model="form.businessRemark"></el-input>
    </el-form-item>
  </el-form>
</template>
<script>
import server from "@/utils/server";

export default {
  name: 'filedetail',
  data() {
    return {
      selectedFile: null,
      form: {
        id: null,
        fileName: "",
        storageName: "",
        fileSize: null,
        fileType: "",
        fileSuffix: "",
        storageService: null,
        domainCode: "",
        subdomainCode: "",
        businessCode: "",
        bucketName: "",
        bucketDomain: "",
        fileDirectory: "",
        fileLeafDirectory: "",
        businessRemark: ""
      }
    }
  },
  methods: {
    clearDetail(){
      this.form = {
        id: null,
        fileName: "",
        storageName: "",
        fileSize: null,
        fileType: "",
        fileSuffix: "",
        storageService: null,
        domainCode: "",
        subdomainCode: "",
        businessCode: "",
        bucketName: "",
        bucketDomain: "",
        fileDirectory: "",
        fileLeafDirectory: "",
        businessRemark: ""
      }
    },
    getDetail(){
      if (!this.form.id) {
        alert('请输入文件ID');
        return false;
      }
      server.get("/api/common/multiple/file/detail/"+this.form.id)
          .then(res=>{
            debugger
            if (res.status === 200) {
              if(null == res.data.body){
                this.$message.warning({
                  message: "文件不存在!"
                })
                return false;
              }
              this.form = res.data.body
              this.$message.success({
                message: "查询成功"
              })
            }
          }).catch(err=>{
        this.$message.warning({
          message:"文件查询失败,请检查后再操作！"
        })
      })

    }
  }
}
</script>
