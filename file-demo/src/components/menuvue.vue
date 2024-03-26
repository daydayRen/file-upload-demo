<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="文件配置" name="config">
      <fileconfig></fileconfig>
    </el-tab-pane>
    <el-tab-pane label="文件上传" name="form">
      <file-form></file-form>
    </el-tab-pane>
<!--    <el-tab-pane label="文件批量上传" name="formbatch">
      <fileform-batch></fileform-batch>
    </el-tab-pane>-->
    <el-tab-pane label="文件下载" name="download">
      <filedownload></filedownload>
    </el-tab-pane>
    <el-tab-pane label="文件详情" name="detail">
      <filedetail></filedetail>
    </el-tab-pane>
  </el-tabs>
</template>
<script>
import FileForm from "@/components/file/FileForm.vue";
import Filedownload from "@/components/file/FileDownload.vue";
import Filedetail from "@/components/file/FileDetail.vue";
import Fileconfig from "@/components/file/FileConfig.vue";
import FileformBatch from "@/components/file/FileFormBatch.vue";
import CreateFileCofig from "@/components/file/childdialog/CreateFileCofig.vue";

export default {
  components: {CreateFileCofig, FileformBatch, Fileconfig, Filedetail, Filedownload, FileForm},
  data() {
    return {
      activeName: 'config', // 当前激活的tab
    };
  },
  methods: {
    handleClick(tab) {
      let name = tab.props.name;
      this.activeName = name;
    },
  },
};
</script>