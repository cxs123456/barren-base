<template>
  <div class="app-container">
    <avue-crud
      ref="crud"
      :option="option"
      v-model="form"
      :page.sync="page"
      :table-loading="loading"
      :data="data"
      @on-load="getList"
      @row-update="rowUpdate"
      @row-save="rowSave"
      @row-del="rowDel"
      :before-open="beforeOpen"
      @refresh-change="refreshChange"
      @search-reset="searchChange"
      @search-change="searchChange"
    />
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import {add, del, getList, update, userRoleList} from '@/api/system/user'
  import {list} from '@/api/system/sysRole'
  import {formatTime} from '@/utils'

  export default {
    name: 'User',
    data() {
      return {
        page: {},
        form: {},
        params: {},
        loading: false,
        data: [],
        roleDicData: [],// 角色字段数据
        option: {
          rowKey: 'id',
          selection: true, // 显示多选
          reserveSelection: true, // 翻页多选
          tip: false, // 多选提示
          index: true,
          indexLabel: '序号',
          card: true, // 卡片模式
          align: 'center',
          headerAlign: 'center',
          border: true,
          stripe: true,
          highlightCurrentRow: true, // 选择行高亮
          viewBtn: true, // 开启查看按钮
          printBtn: true, // 打印按钮
          excelBtn: true, // 导出按钮
          column: [
            {
              label: '用户名',
              prop: 'username',
              search: true, // 设置字段搜索
              rules: [{
                required: true,
                message: '请输入必选项的值',
                trigger: 'blur'
              }]
            },
            {
              label: '昵称',
              prop: 'nickName',
              search: true,
              html: true,
              formatter: (row) => {
                return '<b style="color:red">' + row.nickName + '</b>'
              },
              rules: [{
                required: true,
                message: '请输入必选项的值',
                trigger: 'blur'
              }]
            },
            {
              label: '手机号',
              prop: 'phone',
              search: true,
              rules: [{
                required: true,
                message: '请输入必选项的值',
                trigger: 'blur'
              }]
            },
            {
              label: '邮箱',
              prop: 'email',
              overHidden: true,
              search: true,
              rules: [{
                required: true,
                message: '请输入必选项的值',
                trigger: 'blur'
              }]
            },
            {
              label: '性别',
              prop: 'sex',
              value: 1, // 设置默认值
              type: 'radio',
              dicData: [{label: '男', value: 1}, {label: '女', value: 2}]

            },
            {
              label: '状态',
              prop: 'status',
              value: 0,
              type: 'switch',
              props: {
                label: 'name',
                value: 'code'
              },
              dicData: [{name: '启用', code: 1}, {name: '禁用', code: 0}],
              html: true,
              formatter: (row, value, label) => {
                return '<b style="color:red">' + label + '</b>'
              }
            },
            {
              label: '创建时间',
              prop: 'createTime',
              addDisplay: false, // 新增表单不显示
              editDisplay: false, // 编辑表单不显示
              display: false, // 弹出框表单不显示
              formatter: (row, value, label) => {
                return formatTime(new Date(value))
              }
            },
            {
              label: '角色',
              prop: 'roleIds',
              type: 'select',
              multiple: true,
              span: 24,
              hide: true,
              dicData: [{
                label: '字典1',
                value: 0
              }, {
                label: '字典2',
                value: 1
              }]
            }
          ]
        }
      }
    },
    computed: {
      ...mapGetters(['userInfo'])
    },
    methods: {
      getList() {
        this.loading = true
        const data = Object.assign({
          current: this.page.currentPage,
          size: this.page.pageSize
        }, this.params)
        this.data = []
        getList(data).then(res => {
          const data = res.data;
          this.loading = false
          this.page.total = data.total
          const result = data.records
          this.data = result
        })
      },
      rowSave(row, done, loading) {
        add(Object.assign({
          createUser: this.userInfo.id
        }, row)).then(() => {
          this.$message.success('新增成功')
          done()
          this.getList()
        }).catch(() => {
          loading()
        })
      },
      rowUpdate(row, index, done, loading) {
        update(Object.assign({
          updateUser: this.userInfo.id
        }, row)).then(() => {
          this.$message.success('修改成功')
          done()
          this.getList()
        }).catch(() => {
          loading()
        })
      },
      rowDel(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return del([row.id])
        }).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      },
      searchChange(params, done) {
        if (done) done()
        this.params = params
        this.page.currentPage = 1
        this.getList()
        this.$message.success('搜索成功')
      },
      refreshChange() {
        this.getList()
        this.$message.success('刷新成功')
      },
      beforeOpen(done, type) {
        var prop = this.findObject(this.option.column, 'roleIds');
        prop.dicData = this.roleDicData;
        let form = this.form;
        if (form.id) {
          userRoleList({userId: form.id}).then(res => {
            let list = res.data
            let roleIds = list.map(item => item.roleId)
            this.form.roleIds = roleIds
            done();
          })
        } else {
          done();
        }
      },
    },
    mounted() {
      // 查询所有角色
      list().then(res => {
        let list = res.data
        let dicData = list.map(item => {
          return {label: item.name, value: item.id}
        });
        this.roleDicData = dicData
        var prop = this.findObject(this.option.column, 'roleIds');
        prop.dicData = this.roleDicData;
      })
    },
    created() {

    },
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
