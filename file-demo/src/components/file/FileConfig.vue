<template>
  <el-container>
    <el-header height="300px">
      <el-form ref="form" :model="form" label-width="110px">
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
          <!--          <el-col :span="8"><div class="grid-content bg-purple-light"></div>
                      <el-form-item label="禁用状态">
                        <el-input v-model="form.disabled" ></el-input>
                      </el-form-item></el-col>-->
        </el-row>

        <!--        <el-row>
                  <el-col :span="8"><div class="grid-content bg-purple"></div>
                    <el-form-item label="有效开始时间">
                      <el-input v-model="form.validStartTime" ></el-input>
                    </el-form-item></el-col>
                  <el-col :span="8"><div class="grid-content bg-purple-light"></div>
                    <el-form-item label="有效结束时间">
                      <el-input v-model="form.validEndTime" ></el-input>
                    </el-form-item></el-col>
                </el-row>-->

        <el-button style="margin-left: 10px; margin-bottom: 15px" type="primary" @click="saveConfig">保存</el-button>
        <el-button style="margin-left: 10px; margin-bottom: 15px" type="warning" @click="getFileConfigList">刷新列表
        </el-button>
      </el-form>
    </el-header>
    <el-main>
      <el-table :data="configList" style="width: 100%;" align="left" max-height="250">
        <el-table-column prop="domainCode" label="域code" width="150"></el-table-column>
        <el-table-column prop="domainName" label="域名称" width="120"></el-table-column>
        <el-table-column prop="subdomainCode" label="子域code" width="120"></el-table-column>
        <el-table-column prop="subdomainName" label="子域名称" width="120"></el-table-column>
        <el-table-column prop="businessCode" label="文件业务code" width="120"></el-table-column>
        <el-table-column prop="businessName" label="文件业务名称" width="120"></el-table-column>
        <el-table-column label="存储介质" width="100%" key="slot">
          <template #default="scope">
            {{ scope.storageService === 1 ? 'OBS' : 'FTP' }}
          </template>
        </el-table-column>
        <el-table-column prop="bucketName" label="桶名称" width="120"></el-table-column>
        <el-table-column prop="bucketDomain" label="桶路径" width="120"></el-table-column>
        <el-table-column prop="fileDirectory" label="文件存储目录" width="120"></el-table-column>
        <!--        <el-table-column prop="disabled" label="禁用状态" width="120"></el-table-column>
                <el-table-column prop="validStartTime" label="有效开始时间" width="120"></el-table-column>
                <el-table-column prop="validEndTime" label="有效结束时间" width="120"></el-table-column>-->

        <el-table-column label="操作" width="100%" key="slot">
          <template #default="scope">
            <el-button
                size="small"
                type="danger"
                @click="deleteRow(scope.$index, scope.row)">删除
            </el-button>
          </template>
          <!--          <template slot-scope="scope">

                    </template>-->
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>


</template>
<script>
import server from "@/utils/server";

export default {
  name: 'fileconfig',
  data() {
    return {
      selectedFile: null,
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
        disabled: '',
        validStartTime: '',
        validEndTime: ''
      },
      configList: []
    }
  },
  methods: {
    deleteRow(index, rows) {
      server.delete('/api/common/multiple/file/deleteFileConfig/' + rows.Id).then(res => {
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

    saveConfig() {
      if (!this.form.domainCode) {
        this.$message.warning({
          message: "请填写配置信息"
        })
        return false;
      }
      server.post("/api/common/multiple/file/saveFileConfig", JSON.stringify(this.form), {
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
    },

    getFileConfigList() {
      server.get("/api/common/multiple/file/getFileConfigList")
          .then(res => {
            debugger
            if (res.status === 200) {
              if (null == res.data.body) {
                this.$message.warning({
                  message: "文件配置不存在!"
                })
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
    this.getFileConfigList();
  }
}
</script>
