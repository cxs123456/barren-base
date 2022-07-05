<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <span>About me</span>
    </div>

    <div class="user-profile">
      <div class="box-center">
        <pan-thumb :image="user.avatar" :height="'100px'" :width="'100px'" :hoverable="false">
          <div>Hello</div>
          {{ user.role }}
        </pan-thumb>
      </div>
      <div class="box-center">
        <div class="user-name text-center">{{ user.name }}</div>
        <div class="user-role text-center text-muted">{{ user.role | uppercaseFirst }}</div>
      </div>
    </div>

    <div class="user-bio">
      <div class="user-education user-bio-section">
        <div class="user-bio-section-header">
          <svg-icon icon-class="education"/>
          <span>Education</span></div>
        <div class="user-bio-section-body">
          <div class="text-muted">
            JS in Computer Science from the University of Technology
          </div>
        </div>
      </div>

      <div class="user-skills user-bio-section">
        <div class="user-bio-section-header">
          <svg-icon icon-class="skill"/>
          <span>Skills</span></div>
        <div class="user-bio-section-body">
          <div class="progress-item">
            <span>Vue</span>
            <el-progress :percentage="70"/>
          </div>
          <div class="progress-item">
            <span>JavaScript</span>
            <el-progress :percentage="18"/>
          </div>
          <div class="progress-item">
            <span>Css</span>
            <el-progress :percentage="12"/>
          </div>
          <div class="progress-item">
            <span>ESLint</span>
            <el-progress :percentage="100" status="success"/>
          </div>
        </div>
      </div>

      <div class="user-skills user-bio-section">
        <div class="user-bio-section-header">
          <svg-icon icon-class="anq"/>
          <span>安全设置</span></div>
        <div class="user-bio-section-body">
          <el-link type="warning" @click="showPassDialog">修改密码</el-link>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
  import {mapGetters} from 'vuex'
  import PanThumb from '@/components/PanThumb'
  import {updatePass} from '@/api/system/user'


  export default {
    name: "userCard",
    components: {PanThumb},
    props: {
      user: {
        type: Object,
        default: () => {
          return {
            name: '',
            email: '',
            avatar: '',
            role: ''
          }
        }
      }
    },
    data() {
      // 2次密码确认
      var validateConfirmPass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } /*else if (value !== this.form.newPass) {
          callback(new Error('两次输入密码不一致!'));
        }*/ else {
          callback();
        }
      };

      return {
        form: {},
        option: {
          span: 24,
          column: [
            {
              label: "旧密码",
              prop: "oldPass",
              type: 'password',
              rules: [{
                required: true,
                message: "请输入旧密码",
                trigger: "blur"
              }],
            },
            {
              label: "新密码",
              prop: "newPass",
              type: 'password',
              rules: [{
                required: true,
                message: "请输入新密码",
                trigger: "blur"
              }],
            },
            {
              label: "确认密码",
              prop: "confirmPass",
              type: 'password',
              rules: [{validator: validateConfirmPass, trigger: 'blur'}],
            },
          ]
        }
      }
    },
    computed: {
      ...mapGetters(['userInfo'])
    },
    methods: {
      showPassDialog() {
        this.$DialogForm.show({
          title: '修改',
          width: '30%',
          menuPosition: 'right',
          option: this.option,
          data: this.form,
          beforeClose: (done) => {
            done()
          },
          callback: (res) => {
            let oldPass = res.data.oldPass;
            let newPass = res.data.newPass;
            let confirmPass = res.data.confirmPass;
            if (newPass !== confirmPass) {
              this.$message.error('两次输入密码不一致!');
              res.done()
            } else {
              let param = {id: this.userInfo.id, oldPass: oldPass, newPass: newPass};
              updatePass(param).then(response => {
                this.$message.success('修改成功')
                res.close()
              }).catch(err => {
                res.done()
              });
            }
          }
        })

      },

      async updatePass(data) {
        let res = await updatePass(data);
        return res
      }
    }
  }
</script>

<style lang="scss" scoped>
  .box-center {
    margin: 0 auto;
    display: table;
  }

  .text-muted {
    color: #777;
  }

  .user-profile {
    .user-name {
      font-weight: bold;
    }

    .box-center {
      padding-top: 10px;
    }

    .user-role {
      padding-top: 10px;
      font-weight: 400;
      font-size: 14px;
    }

    .box-social {
      padding-top: 30px;

      .el-table {
        border-top: 1px solid #dfe6ec;
      }
    }

    .user-follow {
      padding-top: 20px;
    }
  }

  .user-bio {
    margin-top: 20px;
    color: #606266;

    span {
      padding-left: 4px;
    }

    .user-bio-section {
      font-size: 14px;
      padding: 15px 0;

      .user-bio-section-header {
        border-bottom: 1px solid #dfe6ec;
        padding-bottom: 10px;
        margin-bottom: 10px;
        font-weight: bold;
      }
    }
  }
</style>
