<template>
  <div>
    <el-container>
      <el-header height="300px">
        <el-form ref="screenForm" :model="form" label-width="110px">
          <el-row>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="域code">
                <el-input v-model="screenForm.domainCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple-light"></div>
              <el-form-item label="域名称">
                <el-input v-model="screenForm.domainName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="子域code">
                <el-input v-model="screenForm.subdomainCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="子域名称">
                <el-input v-model="screenForm.subdomainName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple-light"></div>
              <el-form-item label="文件业务code">
                <el-input v-model="screenForm.businessCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="文件业务名称">
                <el-input v-model="screenForm.businessName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="存储介质">
                <el-select v-model="screenForm.storageService" placeholder="请选择">
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
                <el-input v-model="screenForm.bucketName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="桶路径">
                <el-input v-model="screenForm.bucketDomain"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="文件存储目录">
                <el-input v-model="screenForm.fileDirectory"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <div class="grid-content bg-purple-light"></div>
              <el-form-item label="禁用状态">
                <el-switch v-model="screenForm.disabled" :active-value="0" :inactive-value="1" active-text="启用"
                           inactive-text="禁用"></el-switch>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
              <el-form-item label="有效开始时间">
                <el-date-picker
                    v-model="screenForm.validStartTime"
                    type="datetime"
                    placeholder="选择日期时间"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple-light"></div>
              <el-form-item label="有效结束时间">
                <el-date-picker
                    v-model="screenForm.validEndTime"
                    type="datetime"
                    placeholder="选择日期时间"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-button style="margin-left: 10px; margin-bottom: 15px" type="warning" @click="resetGet">重 置
          </el-button>
          <el-button style="margin-left: 10px; margin-bottom: 15px" type="success" @click="getFileConfigList">查 询
          </el-button>
        </el-form>
      </el-header>
      <el-main>
        <el-button @click="showDialog" style="display: flex;justify-content: flex-start;margin-bottom: 10px"
                   type="primary">新建
        </el-button>
        <el-table :data="configList" stripe border style="width: 100%;" align="left" max-height="470">
          <el-table-column prop="domainCode" label="域code" width="150"></el-table-column>
          <el-table-column prop="domainName" label="域名称" width="120"></el-table-column>
          <el-table-column prop="subdomainCode" label="子域code" width="120"></el-table-column>
          <el-table-column prop="subdomainName" label="子域名称" width="120"></el-table-column>
          <el-table-column prop="businessCode" label="文件业务code" width="120"></el-table-column>
          <el-table-column prop="businessName" label="文件业务名称" width="120"></el-table-column>
          <el-table-column label="存储介质" width="100%" key="slot">
            <template #default="scope">
              {{ scope.row.storageService === 1 ? 'OBS' : 'FTP' }}
            </template>
          </el-table-column>
          <el-table-column prop="bucketName" label="桶名称" width="120"></el-table-column>
          <el-table-column prop="bucketDomain" label="桶路径" width="120"></el-table-column>
          <el-table-column prop="fileDirectory" label="文件存储目录" width="120"></el-table-column>
          <el-table-column label="禁用状态" width="100" key="slot">
            <template #default="scope">
              {{ scope.row.disabled === 1 ? '禁用' : '启用' }}
            </template>
          </el-table-column>
          <el-table-column label="有效开始时间" width="120" key="slot">
            <template #default="scope">
              <!-- 使用moment.js库进行格式化 -->
              <span>{{ formatDate(scope.row.validStartTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="有效结束时间" width="120" key="slot">
            <template #default="scope">
              <!-- 使用moment.js库进行格式化 -->
              <span>{{ formatDate(scope.row.validEndTime) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="200" key="slot">
            <template #default="scope">
              <el-button
                  size="small"
                  type="danger"
                  @click="deleteRow(scope.$index, scope.row)">删除
              </el-button>

              <el-button
                  size="small"
                  type="primary"
                  @click="updateRow(scope.$index, scope.row)">编辑
              </el-button>
            </template>
            <!--          <template slot-scope="scope">

                      </template>-->
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
    <!-- el-dialog 用于显示表单 -->
    <create-file-cofig v-model="isFormVisible"></create-file-cofig>
    <update-file-cofig v-model="isUpdateFormVisible" :update-form="parentUpdateForm"></update-file-cofig>
  </div>
</template>
<script>
import server from "@/utils/server";
import CreateFileCofig from "@/components/file/childdialog/CreateFileCofig.vue";
import UpdateFileCofig from "@/components/file/childdialog/updateFileCofig.vue";

export default {
  name: 'fileconfig',
  components: {UpdateFileCofig, CreateFileCofig},
  data() {
    return {
      isFormVisible: false,
      isUpdateFormVisible: false,
      dialogForm: {
        username: '',
        email: ''
      },
      parentUpdateForm: null,
      selectedFile: null,
      screenForm: {
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
        validEndTime: '',
      },
      configList: [],
      options: [{
        value: '2',
        label: 'FTP'
      }, {
        value: '1',
        label: 'OBS'
      }],
    }
  },
  methods: {
    handleStartDateChange(value) {
      this.screenForm.validStartTime = value;
    },
    updateShowModal() {
      this.isFormVisible = false;
      this.getFileConfigList();
    },
    updateShowUpdaeModal() {
      this.isUpdateFormVisible = false;
      this.getFileConfigList();
    },
    showDialog() {
      this.isFormVisible = true;
    },
    deleteRow(index, rows) {
      server.delete('/api/common/multiple/fileConfig/deleteFileConfig/' + rows.Id).then(res => {
        if (res.status === 200) {
          this.$message.success({
            message: "删除成功"
          })
          this.getFileConfigList()
        }
      }).catch(err => {
        this.$message.warning({
          message: "文件删除失败,请检查后再操作！"
        })
      })
    },
    formatDate(timestamp) {
      if (!timestamp)
        return '';
      // return moment(timestamp).format('YYYY-MM-DD HH:mm:ss'); // 格式化为年-月-日 时:分:秒
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const hour = date.getHours();
      const minute = date.getMinutes();
      const second = date.getSeconds();
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    },
    updateRow(index, rows) {
      this.isUpdateFormVisible = true;
      this.parentUpdateForm = rows;
    },

    /*saveConfig() {
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
            this.getFileConfigList();
          }).catch(err => {
        this.$message.error({
          message: "保存配置失败,请检查后再操作！"
        })
      })
    },*/

    resetGet(){
      this.screenForm = {
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
        disabled: 0,
        validStartTime: '',
        validEndTime: '',
      }
    },
    getFileConfigList() {
      let search = {domainCode:null};
      if(!this.screenForm || Object.values(this.screenForm).every(value => !value && value !== 0)){
        search = {...this.screenForm};
      }
      server.post("/api/common/multiple/fileConfig/getFileConfigList", JSON.stringify(this.screenForm), {
            headers: {
              "Content-Type": "application/json"
            }
          }
      )
          .then(res => {
            if (res.status === 200) {
              if (null == res.data.body) {
                this.$message.warning({
                  message: "文件配置不存在!"
                })
                this.configList = [];
                return false;
              }
              this.configList = res.data.body
              this.$message.success({
                message: "查询成功"
              })
            }
          }).catch(err => {
        this.$message.warning({
          message: "文件配置查询失败,请检查后再操作！"
        })
      })
    },
  },
  mounted() {
    this.screenForm.disabled = 0;
    this.getFileConfigList();
  }
}
</script>
