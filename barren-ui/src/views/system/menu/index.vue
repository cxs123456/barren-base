<template>
  <div class="app-container">
    <avue-crud
      ref="crud"
      :option="option"
      v-model="form"
      :table-loading="loading"
      :data="data"
      @on-load="getList"
      @row-update="rowUpdate"
      @row-save="rowSave"
      :before-open="beforeOpen"
      @row-del="rowDel"
      @refresh-change="refreshChange"
      @search-reset="searchChange"
      @search-change="searchChange"
      >
      <template
        slot-scope="{type,disabled}"
        slot="iconForm">
        <el-popover
          placement="bottom-start"
          width="600"
          trigger="click"
          @show="$refs['iconSelect'].reset()"
          >
          <IconSelect
            ref="iconSelect"
            @selected="selected" />
          <el-input
            slot="reference"
            v-model="form.icon"
            style="width: 450px;"
            placeholder="点击选择图标"
            readonly>
            <svg-icon
              v-if="form.icon"
              slot="prefix"
              :icon-class="form.icon"
              class="el-input__icon"
              style="height: 32px;width: 16px;" />
            <i
              v-else
              slot="prefix"
              class="el-icon-search el-input__icon" />
          </el-input>
        </el-popover>
      </template>
      <template
        slot="icon"
        slot-scope="scope">
        <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''" />
      </template>

    </avue-crud>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import {add, del, getTreeList, update, lazyTreeList, allTreeList} from "@/api/system/sysMenu"
  import Treeselect, {LOAD_CHILDREN_OPTIONS} from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import IconSelect from '@/components/IconSelect'


  export default {
    name: "SysMenu",
    components: {Treeselect, IconSelect},
    data() {
      return {
        form: {},
        params: {},
        loading: false,
        data: [],
        menus: [],
        option: {
          rowKey: 'id',
          rowParentKey: 'pid',
          defaultExpandAll: false,
          selection: true, // 显示多选
          tip: false, // 多选提示
          index: true,
          indexLabel: '序号',
          card: true, // 卡片模式
          align: 'center',
          headerAlign: 'center',
          border: true,
          stripe: true,
          highlightCurrentRow: true, // 选择行高亮
          printBtn: true, // 打印按钮
          excelBtn: true, // 导出按钮
          searchMenuSpan: 4,// 设置搜索按钮宽度
          emptyBtnText: '重置',
          dialogWidth: '40%',  // 弹框宽度
          column: [
            {
              label: "菜单名称",
              prop: "title",
              order: 99,
              span: 12,
              search: true,
              searchSpan: 6, // 设置搜索字段宽度
              rules: [{
                required: true,
                message: "请输入必填项的值",
                trigger: "blur"
              }]
            },
            {
              label: "菜单图标",
              prop: "icon",
              span: 24,
              formslot: true,
              display: true,
              slot: true,
              order: 97,
            },
            {
              label: "排序",
              prop: "sort",
              value: 999,
              type: 'number'
            },
            {
              label: "菜单类型",
              prop: "type",
              value: 0,
              hide: true,
              span: 24,
              type: 'radio',
              order: 100,
              dicData: [{label: '目录', value: 0}, {label: '菜单', value: 1}, {label: '按钮', value: 2}],
              control: (val, form) => {  // 控制form其他字段显示
                if (val == 0) {
                  return {
                    cache: {
                      display: false
                    },
                    name: {
                      display: false
                    },
                    component: {
                      display: false
                    },
                    permission: {
                      display: false
                    },
                    icon: {
                      display: true
                    },
                    path: {
                      display: true
                    },
                    iFrame: {
                      display: true
                    },
                    hidden: {
                      display: true
                    },
                  }
                } else if (val == 1) {
                  return {
                    cache: {
                      display: true
                    },
                    name: {
                      display: true
                    },
                    component: {
                      display: true
                    },
                    permission: {
                      display: true
                    },
                    icon: {
                      display: true
                    },
                    path: {
                      display: true
                    },
                    iFrame: {
                      display: true
                    },
                    hidden: {
                      display: true
                    },
                  }
                } else {
                  return {
                    cache: {
                      display: false
                    },
                    name: {
                      display: false
                    },
                    component: {
                      display: false
                    },
                    permission: {
                      display: true
                    },
                    icon: {
                      display: false
                    },
                    path: {
                      display: false
                    },
                    iFrame: {
                      display: false
                    },
                    hidden: {
                      display: false
                    },
                  }
                }
              }
            },
            {
              label: "路由地址",
              prop: "path",
              hide: true,
              disply: true,
              order: 96,
              rules: [{
                required: true,
                message: "请输入必填项的值",
                trigger: "blur"
              }]
            },
            {
              label: "组件名称",
              prop: "name",
              disply: true
            },
            {
              label: "组件路径",
              prop: "component",
              disply: true
            },
            {
              label: "权限标识",
              prop: "permission",
              display: true,
              order: 98
            },
            {
              label: "是否外链",
              prop: "iFrame",
              value: false,
              type: 'switch',
              disply: true,
              span: 8,
              dicData: [{label: '是', value: true}, {label: '否', value: false}]
            },
            {
              label: "缓存",
              prop: "cache",
              value: false,
              display: true,
              type: 'switch',
              span: 8,
              dicData: [{label: '是', value: true}, {label: '否', value: false}]
            },
            {
              label: "可见",
              prop: "hidden",
              value: false,
              type: 'switch',
              disply: true,
              span: 8,
              dicData: [{label: '否', value: true}, {label: '是', value: false}]
            },
            {
              label: "上级目录",
              prop: "pid",
              type: 'tree',
              span: 24,
              hide: true, // 列表隐藏该列
              value: 0,
              filter: true,
              tags: true,
              //formslot: true, // 自定义表单项
              dicData: []
            },
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay: false, // 新增表单不显示
              editDisplay: false, // 编辑表单不显示
              rules: [{
                required: true,
                message: "请输入必填项的值",
                trigger: "blur"
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
        this.loading = true;
        let params = this.params;
        getTreeList(params).then(res => {
          const result = res.data;
          this.loading = false;
          this.data = result;
        })
      },
      rowSave(row, done, loading) {
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
      // 点击搜索调用方法
      searchChange(params, done) {
        if (done) done();
        this.params = params;
        this.getList();
        this.$message.success('搜索成功')
      },
      refreshChange() {
        this.getList();
        this.$message.success('刷新成功')
      },
      beforeOpen(done, type) {
        let form = this.form;
        // 设置默认值为0，显示顶层目录
        form.pid = form.pid || 0
        allTreeList().then(res => {
          let list = res.data
          this.menus = [{value: 0, label: '顶级类目', children: list}]
          //var option = {column:[]}
          var prop = this.findObject(this.option.column, 'pid');
          prop.dicData = this.menus
          done();
        })

      },
      loadMenus({action, parentNode, callback}) {
        if (action === LOAD_CHILDREN_OPTIONS) {
          lazyTreeList(parentNode.id).then(res => {
            let list = res.data
            if (!list) {
              parentNode.children = null
            } else {
              parentNode.children = list.map(function (obj) {
                // if (!obj.leaf) {
                //   obj.children = null
                // }
                obj.label = obj.title
                return obj
              })
            }
            setTimeout(() => {
              callback()
            }, 100)
          })
        }
      },
      // 选中图标
      selected(name) {
        this.form.icon = name
      }
    },
    mounted() {

    },
    created() {

    },
  }
</script>

<style
  rel="stylesheet/scss"
  lang="scss"
  scoped>

  ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }

  ::v-deep .vue-treeselect__control, ::v-deep .vue-treeselect__placeholder, ::v-deep .vue-treeselect__single-value {
    height: 30px;
    line-height: 30px;
  }
</style>
