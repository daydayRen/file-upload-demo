<template>
  <el-form ref="form" :model="form" label-width="110px">
    <el-form-item label="文件id">
      <el-input v-model="form.fileId" type="number" placeholder="请输入数字" ></el-input>
    </el-form-item>
    <el-form-item label="下载人ID">
      <el-input v-model="form.downloadUserId" type="number" placeholder="请输入数字" ></el-input>
    </el-form-item>
    <el-form-item label="下载人姓名">
      <el-input v-model="form.downloadUserName"></el-input>
    </el-form-item>
    <el-button style="margin-left: 10px;" type="primary" @click="submitUpload">下载</el-button>
    <el-button style="margin-left: 10px;" type="warning" @click="deletFile">删除</el-button>
  </el-form>
</template>
<script>
import server from "@/utils/server";
export default {
  name: 'filedownload',
  data() {
    return {
      selectedFile:null,
      form: {
        fileId: '',
        downloadUserId: '',
        downloadUserName: '',
      }
    }
  },
  methods: {
    submitUpload() {
      if (!this.form.fileId || !this.form.downloadUserId || !this.form.downloadUserName) {
        alert("请输入所有数据信息")
        return
      }
      server.post('/api/common/multiple/file/download', this.form, {
        responseType: 'blob'
      }).then(res => {
        debugger
        console.log(res)
        let fileName = decodeURI((res.headers['content-disposition'].split("="))[1]);
        const blob = new Blob([res.data])//返回一个新的blob对象
        const downLoadElement = document.createElement('a')//创建a标签
        downLoadElement.style.display = 'none'//a标签样式为隐藏
        const href = window.URL.createObjectURL(blob)//创建window临时地址
        downLoadElement.href = href//跳转地址
        downLoadElement.setAttribute('download', fileName)
        document.body.appendChild(downLoadElement)//将指定的dom添加的body之后
        downLoadElement.click()//点击事件
        document.body.removeChild(downLoadElement)//移除dom
        window.URL.revokeObjectURL(href)//释放url地址
      })
    },
    deletFile(){
      if (!this.form.fileId) {
        alert("请输入文件ID")
        return
      }
      server.delete('/api/common/multiple/file/delete/'+this.form.fileId).then(res => {
        if (res.status === 200) {
          this.$message.success({
            message: "删除成功"
          })
        }
      }).catch(err =>{
        this.$message.warning({
          message:"文件查询失败,请检查后再操作！"
        })
      })
    }
  }
}
</script>
