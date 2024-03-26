<!-- MyModalComponent.vue -->
<template>
  <el-dialog :modelValue="showModal" title="新增文件配置" :append-to-body="true" width="1200px"  :close-on-click-modal="false"
             :close-on-press-escape="false" >
    <el-form ref="form" :model="form" label-width="110px" style="margin-top: 10px">
      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="域code">
            <el-input v-model="form.domainCode"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="域名称">
            <el-input v-model="form.domainName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="子域code">
            <el-input v-model="form.subdomainCode"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="子域名称">
            <el-input v-model="form.subdomainName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="文件业务code">
            <el-input v-model="form.businessCode"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="文件业务名称">
            <el-input v-model="form.businessName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="存储介质">
            <el-input v-model="form.storageService" type="number" placeholder="请输入数字 1:OBS 2:FTP"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="桶名称">
            <el-input v-model="form.bucketName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="桶路径">
            <el-input v-model="form.bucketDomain"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="文件存储目录">
            <el-input v-model="form.fileDirectory"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="禁用状态">
            <el-switch v-model="form.disabled" :active-value="0" :inactive-value="1" active-text="启用" inactive-text="禁用"></el-switch>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="有效开始时间">
            <el-date-picker
                v-model="form.validStartTime"
                type="datetime"
                placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="有效结束时间">
            <el-date-picker
                v-model="form.validEndTime"
                type="datetime"
                placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
    <el-button type="warning" @click="cancelModal">取 消</el-button>
    <el-button type="primary" @click="saveConfig">保 存</el-button>
  </span>
    </el-form>
  </el-dialog>
</template>

<script>
import server from "@/utils/server";

export default {
  name: "CreateFileCofig",
  props: {
    showModal: false,
  },
  data() {
    return {

      form: {
        domainCode: '',
        domainName: '',
        subdomainCode: '',
        subdomainName: '',
        businessCode: '',
        businessName: '',
        storageService: '',
        bucketName: '',
        bucketDomain: '',
        fileDirectory: '',
        disabled: '0',
        validStartTime: '',
        validEndTime: ''
      },
    };
  },
  methods: {
    cancelModal() {
     this.$parent.updateShowModal();
    },

    saveConfig() {
      if (!this.form.domainCode) {
        this.$message.warning({
          message: "请填写配置信息"
        })
        return false;
      }
      server.post("/api/common/multiple/fileConfig/saveOrUpdateFileConfig", JSON.stringify(this.form), {
        headers: {
          "Content-Type": "application/json"
        }
      })
          .then(res => {
            if (res.status === 200) {
              this.$message.success({
                message: "保存成功"
              })
            }
            this.$parent.updateShowModal();
          }).catch(err => {
        this.$message.error({
          message: "保存配置失败,请检查后再操作！"
        })
      })
    },
  },
  created() {
    this.form = {
      domainCode: '',
      domainName: '',
      subdomainCode: '',
      subdomainName: '',
      businessCode: '',
      businessName: '',
      storageService: '',
      bucketName: '',
      bucketDomain: '',
      fileDirectory: '',
      disabled: '0',
      validStartTime: '',
      validEndTime: ''
    }
  },
  mounted() {
    this.form.disabled = 0;
  }
};
</script>


<style>

.dialog-footer {
  margin-left: 500px;
}

</style>