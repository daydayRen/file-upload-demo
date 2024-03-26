<!-- MyModalComponent.vue -->
<template>
  <el-dialog :modelValue="showModal" title="修改文件配置" :append-to-body="true" width="1200px"
             :close-on-click-modal="false"
             :close-on-press-escape="false">
    <el-form ref="form" :model="form" label-width="110px" style="margin-top: 10px">
      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="域code">
            <el-input v-model="formData.domainCode"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="域名称">
            <el-input v-model="formData.domainName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="子域code">
            <el-input v-model="formData.subdomainCode"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="子域名称">
            <el-input v-model="formData.subdomainName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="文件业务code">
            <el-input v-model="formData.businessCode"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="文件业务名称">
            <el-input v-model="formData.businessName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="存储介质">
            <el-select v-model="formData.storageService" placeholder="请选择">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="桶名称">
            <el-input v-model="formData.bucketName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="桶路径">
            <el-input v-model="formData.bucketDomain"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="grid-content bg-purple"></div>
          <el-form-item label="文件存储目录">
            <el-input v-model="formData.fileDirectory"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light"></div>
          <el-form-item label="禁用状态">
            <el-switch v-model="formData.disabled" :active-value="0" :inactive-value="1" active-text="启用" inactive-text="禁用"></el-switch>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
          <el-col :span="8">
            <div class="grid-content bg-purple"></div>
            <el-form-item label="有效开始时间">
              <el-date-picker
                  v-model="formData.validStartTime"
                  type="datetime"
                  placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light"></div>
            <el-form-item label="有效结束时间">
              <el-date-picker
                  v-model="formData.validEndTime"
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
  name: "UpdateFileCofig",
  props: {
    showModal: false,
    updateForm:  {
      type: Object,
      default: () => ({})
    },
  },
  data() {
    return {
      formData: {},
      options: [{
        value: 2,
        label: 'FTP'
      }, {
        value: 1,
        label: 'OBS'
      }]
    };

  },
  methods: {
    cancelModal() {
      this.$parent.updateShowUpdaeModal();
    },

    saveConfig() {
      if (!this.formData.domainCode) {
        this.$message.warning({
          message: "请填写配置信息"
        })
        return false;
      }
      this.formData.id = this.formData.Id;
      server.post("/api/common/multiple/fileConfig/saveOrUpdateFileConfig", JSON.stringify(this.formData), {
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
            this.$parent.updateShowUpdaeModal();
          }).catch(err => {
        this.$message.error({
          message: "保存配置失败,请检查后再操作！"
        })
      })
    },
  },
  watch:{
    updateForm(newVal){
      this.formData = JSON.parse(JSON.stringify(newVal));
    }
  },

};
</script>


<style>

.dialog-footer {
  margin-left: 500px;
}

</style>