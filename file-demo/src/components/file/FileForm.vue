<template>
  <el-form ref="form" :model="form" label-width="110px">
    <el-form-item label="域code编码">
      <el-input v-model="form.domainCode"></el-input>
    </el-form-item>
    <el-form-item label="子域code编码">
      <el-input v-model="form.subdomainCode"></el-input>
    </el-form-item>
    <el-form-item label="业务code编码">
      <el-input v-model="form.businessCode"></el-input>
    </el-form-item>
    <el-form-item label="业务id">
      <el-input v-model="form.businessId"></el-input>
    </el-form-item>
    <el-form-item label="业务备注">
      <el-input v-model="form.businessRemark"></el-input>
    </el-form-item>
    <el-upload
        limit="1"
        class="upload-demo"
        drag
        :on-change="handleChange"
        :on-remove="handRemove"
        :auto-upload="false">
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>

      <!--      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
    </el-upload>
    <el-button style="margin-left: 10px;" type="primary" @click="submitUpload">上传到服务器</el-button>
  </el-form>
</template>
<script>
import server from "@/utils/server";

export default {
  name: 'fileform',
  data() {
    return {
      selectedFile: null,
      form: {
        domainCode: 'doCode1',
        subdomainCode: 'subDoCode1',
        businessCode: 'BuCode1',
        businessId: '业务',
        businessRemark: '业务备注',
      }
    }
  },
  methods: {
    submitUpload() {
      if (!this.selectedFile) {
        alert('请选择您要上传的图片~');
        return false;
      }
      let formData = new FormData;
      for (const [key, value] of Object.entries(this.form)) {
        formData.append(key, value);
      }
      formData.append('file', this.selectedFile);
      server.post('/api/common/multiple/file/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(res => {
        debugger
        if (res.status === 200) {
          this.$message.success({
            message: "文件上传成功"
          })
          return;
        }
        return Promise.reject(res.data);
      }).catch(err => {
        this.$message.warning({
          message:"文件上传失败,请检查后再操作！"
        })
            console.log(err);
          })

    },
    handleChange(file, fileList) {
      this.selectedFile = file.raw;
    },
    handRemove(file) {
      this.selectedFile = null;
    }
  }
}
</script>
