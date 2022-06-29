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
import {add, del, getList, update} from "@/api/${package.ModuleName}/${table.entityPath}"

export default {
  name: "${entity}",
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
          <#-- ----------  BEGIN 字段循环遍历  ---------->
          <#list table.fields as field>
          {
            label: "${field.comment}",
            prop: "${field.propertyName}",
            search: true,
            rules: [{
              required: true,
              message: "请输入必填项的值",
              trigger: "blur"
            }]
          },
          </#list>
          <#------------  END 字段循环遍历  ---------->
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
