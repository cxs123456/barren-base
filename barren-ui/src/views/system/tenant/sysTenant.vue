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
import {mapGetters} from "vuex";
import {add, del, getList, update} from "@/api/system/sysTenant"

export default {
  name: "SysTenant",
  data () {
    return {
      page: {},
      form: {},
      params: {},
      loading: false,
      data: [],
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
            label: "租户id",
            prop: "id",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "租户名",
            prop: "name",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "联系人的用户id",
            prop: "contactUserId",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "联系人",
            prop: "contactName",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "联系手机号",
            prop: "contactPhone",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "联系地址",
            prop: "address",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "绑定域名",
            prop: "domain",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "租户套餐编号",
            prop: "packageId",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "过期时间",
            prop: "expireTime",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "账号数量",
            prop: "accountCount",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "授权码",
            prop: "licenseKey",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "创建人",
            prop: "createUser",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "修改人",
            prop: "updateUser",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "创建时间",
            prop: "createTime",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "修改时间",
            prop: "updateTime",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "帐号状态，1启用 0停用",
            prop: "status",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "删除标志，0正常 1删除",
            prop: "delFlag",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          {
            label: "备注",
            prop: "remark",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  methods: {
    getList () {
      this.loading = true;
      const data = Object.assign({
        current: this.page.currentPage,
        size: this.page.pageSize,
      }, this.params);
      this.data = [];
      getList(data).then(res => {
        const data = res.data;
        this.loading = false;
        this.page.total = data.total;
        const result = data.records;
        this.data = result;
      })
    },
    rowSave (row, done, loading) {
      add(Object.assign({
        createUser: this.userInfo.id
      }, row)).then(() => {
        this.$message.success('新增成功');
        done();
        this.getList();
      }).catch(() => {
        loading()
      })
    },
    rowUpdate (row, index, done, loading) {
      update(Object.assign({
        updateUser: this.userInfo.id
      }, row)).then(() => {
        this.$message.success('修改成功');
        done();
        this.getList();
      }).catch(() => {
        loading()
      })
    },
    rowDel (row) {
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return del([row.id])
      }).then(() => {
        this.$message.success('删除成功');
        this.getList();
      })
    },
    searchChange (params, done) {
      if (done) done();
      this.params = params;
      this.page.currentPage = 1;
      this.getList();
      this.$message.success('搜索成功')
    },
    refreshChange () {
      this.getList();
      this.$message.success('刷新成功')
    }
  },
  mounted () {

  },
  created () {

  },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
