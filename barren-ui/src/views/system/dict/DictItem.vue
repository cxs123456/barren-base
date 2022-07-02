<template>
  <div class="app-container">
    <avue-crud
      ref="itemCrud"
      :option="option"
      v-model="form"
      :table-loading="loading"
      :data="data"
      @on-load="getList"
      @row-update="rowUpdate"
      @row-save="rowSave"
      @row-del="rowDel"
      @refresh-change="refreshChange"
      :before-open="beforeOpen"
    >

      <template slot-scope="scope" slot="menuLeft">
        <el-button type="danger"
                   icon="el-icon-plus"
                   size="small"
                   :disabled="addDisabled"
                   @click="$refs.itemCrud.rowAdd()">新增
        </el-button>
      </template>
    </avue-crud>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import {add, del, update, list} from "@/api/system/sysDictItem"

  export default {
    name: "DictItem",
    // props: {
    //   dictId: {
    //     required: true,
    //     default: null,
    //     type: Number
    //   }
    // },
    data() {
      return {
        dictId: 0,
        dictCode: null,
        form: {},
        params: {},
        loading: false,
        addDisabled: true,
        data: [],
        option: {
          rowKey: 'id',
          emptyText: '点击字典查看详情',
          // card: true, // 卡片模式
          align: 'center',
          headerAlign: 'center',
          border: true,
          stripe: true,
          highlightCurrentRow: true, // 选择行高亮
          addBtn: false,
          refreshBtn: true,
          searchBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          dialogWidth: '40%',  // 弹框宽度
          labelWidth:120,
          column: [
            {
              label: "字典编码",
              prop: "dictCode",
              hide: true,
              editDisabled: true,
              addDisabled: true,
              span: 24,
            },
            {
              label: "字典项名称",
              prop: "itemName",
              rules: [{
                required: true,
                message: "请输入必填项的值",
                trigger: "blur"
              }],
              span: 12,
              labelWidth:120
            },
            {
              label: "字典项值",
              prop: "itemValue",
              rules: [{
                required: true,
                message: "请输入必填项的值",
                trigger: "blur"
              }],
              span: 12,
            },
            {
              label: "排序",
              prop: "sort",
              value: 999,
              type: 'number',
            },
            {
              label: "备注",
              prop: "remark",
              type: 'textarea',
              hide: true,
              span: 24,
              maxlength: 100,
              showWordLimit: true,
              overHidden: true,
            },
          ]
        }
      }
    },
    computed: {
      ...mapGetters(['userInfo']),

    },
    methods: {
      getList() {
        if (!this.dictId) {
          return
        }
        // this.form
        this.loading = false;
        list({dictId: this.dictId}).then(res => {
          const result = res.data;
          this.loading = false;
          this.data = result;
        })
      },
      rowSave(row, done, loading) {
        debugger
        if (!this.dictId || !this.dictCode) {
          this.$message.error('请选择字典！');
          return
        }

        add(Object.assign({
          createUser: this.userInfo.id,
          dictId: this.dictId,
          dictCode: this.dictCode
        }, row)).then(() => {
          this.$message.success('新增成功');
          done();
          this.getList();
        }).catch(() => {
          loading()
        })
      },
      rowUpdate(row, index, done, loading) {
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
      rowDel(row) {
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
      refreshChange() {
        this.getList();
        // this.$message.success('刷新成功')
      },
      beforeOpen(done, type) {
        this.form.dictCode = this.dictCode;
        this.form.dictId = this.dictId;
        done();
      },
      // 查询字典列表，父组件调用
      getDictItemList({dictId, dictCode}) {
        if (!dictId) {
          this.$message.warning('请点击字典查看详情');
          return
        }
        this.addDisabled = false;
        this.dictId = dictId;
        this.dictCode = dictCode;
        this.form.dictCode = dictCode;
        this.form.dictId = dictId;
        this.getList();
      }
    },
    mounted() {

    },
    created() {

    },
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
