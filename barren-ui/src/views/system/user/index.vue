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
      @refresh-change="refreshChange"
      @search-reset="searchChange"
      @search-change="searchChange"
    />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getList, update, add, del } from '@/api/system/user'

export default {
  name: 'User',
  data() {
    return {
      page: {},
      form: {},
      params: {},
      loading: false,
      data: [],
      option: {
        index: true,
        align: 'center',
        headerAlign: 'center',
        border: true,
        stripe: true,
        column: [{
          label: 'ID',
          prop: 'id',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '用户名',
          prop: 'username',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '密码',
          prop: 'password',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '昵称',
          prop: 'nickName',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '手机号',
          prop: 'phone',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '邮箱',
          prop: 'email',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '性别',
          prop: 'sex',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '是否为admin账号',
          prop: 'isAdmin',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '头像地址',
          prop: 'avatar',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '最后登录IP',
          prop: 'loginIp',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '最后登录时间',
          prop: 'loginDate',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '创建人',
          prop: 'createUser',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '修改人',
          prop: 'updateUser',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '创建时间',
          prop: 'createTime',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '修改时间',
          prop: 'updateTime',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '帐号状态，1启用 0停用',
          prop: 'status',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '删除标志，0正常 1删除',
          prop: 'delFlag',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }],
          label: '备注',
          prop: 'remark',
          search: true,
          rules: [{
            required: true,
            message: '请输入必选项的值',
            trigger: 'blur'
          }]
        }]
      }
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  mounted() {

  },
  created() {

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
        const data = res.data.data
        this.loading = false
        this.page.total = data.total
        const result = data.list
        this.data = result
      })
    },
    rowSave(row, done, loading) {
      add(Object.assign({
        createUser: this.userInfo.name
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
        updateUser: this.userInfo.name
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
        return del(row.id)
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
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
