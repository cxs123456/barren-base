<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-col :span="16">
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
          @current-row-change="handleCurrentRowChange"
        />
      </el-col>
      <el-col :span="8">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <el-tooltip class="item" effect="dark" content="选择指定角色分配菜单" placement="top">
              <span class="role-span">菜单分配</span>
            </el-tooltip>
            <el-button
              :disabled="!showButton"
              :loading="menuLoading"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 6px 9px"
              type="primary"
              @click="saveMenu"
            >保存
            </el-button>
          </div>
          <el-tree
            ref="menu"
            :data="menus"
            :default-checked-keys="menuIds"
            highlight-current
            show-checkbox
            node-key="id"
            default-expand-all
            check-on-click-node
            check-strictly
            @check="handleMenuChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import {add, del, getList, update, roleMenulist, saveRoleMenus} from "@/api/system/sysRole"
  import {allTreeList} from "@/api/system/sysMenu"

  export default {
    name: "SysRole",
    data() {
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
          // emptyText: '暂无数据',  // 列表无数据提示语
          tip: false, // 多选提示
          index: true,
          indexLabel: '序号',
          card: true, // 卡片模式
          align: 'center',
          headerAlign: 'center',
          border: true,
          stripe: true,
          highlightCurrentRow: true, // 选择行高亮
          // viewBtn: true, // 开启查看按钮
          printBtn: true, // 打印按钮
          excelBtn: true, // 导出按钮
          searchMenuSpan: 6,// 设置搜索按钮宽度
          column: [
            {
              label: "角色名称",
              prop: "name",
              search: true,
              searchSpan: 4,
              rules: [{
                required: true,
                message: "请输入必填项的值",
                trigger: "blur"
              }]
            },
            {
              label: "角色编码",
              prop: "code",
              search: true,
              searchSpan: 4,
              rules: [{
                type: 'string',
                required: true,
                message: "请输入必填项的值",
                trigger: "blur"
              }, {
                type: 'string',
                required: true,
                message: "使用数字和字母的组合",
                pattern: /^[0-9A-Za-z]+$/,
                trigger: "blur"
              }, {
                type: 'string',
                required: true,
                message: "字符长度限制4-10位",
                min: 4,
                max: 10,
                trigger: "blur"
              }]
            },
            {
              label: "角色级别",
              prop: "level",
              value: 1,
              type: 'number',
              disable: true
            },
            {
              label: "数据权限",
              prop: "dataScope",
              value: 0,
              type: 'select',
              dicData: [{label: '全部', value: 0}, {label: '自定义', value: 1}, {label: '本级', value: 2}],
            },
            {
              label: "创建时间",
              prop: "createTime",
              width: 150, // 设置列表页中该列的宽度
              addDisplay: false, // 新增表单不显示
              editDisplay: false // 编辑表单不显示
            },
            {
              label: "备注",
              prop: "remark",
              type: 'textarea',
              span: 24,
              maxlength: 100,
              showWordLimit: true,
              overHidden: true,
            },
          ]
        },
        menuLoading: false,
        showButton: false,
        currentId: 0,  // 当前选择行 id
        menus: [],
        menuIds: [], // 默认选择节点
        selectMenuIds: []
      }
    },
    computed: {
      ...mapGetters(['userInfo'])
    },
    methods: {
      getList() {
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
      searchChange(params, done) {
        if (done) done();
        this.params = params;
        this.page.currentPage = 1;
        this.getList();
        // this.$message.success('搜索成功')
      },
      refreshChange() {
        this.getList();
        // this.$message.success('刷新成功')
      },
      // 选择行事件
      handleCurrentRowChange(row) {
        this.menuIds = []
        this.currentId = row.id
        roleMenulist({roleId: this.currentId}).then((res) => {
          let list = res.data
          let menuIds = list.map(item => item.menuId);
          // 清空菜单tree的选中
          this.$refs.menu.setCheckedKeys([])
          this.menuIds = menuIds;
          // 选择了 行数据 就显示button
          this.showButton = true
        });

      },
      // 菜单选择事件
      handleMenuChange(menu, tree) {
        // 获取该节点的所有子节点，id 包含自身
        // console.log(this.menuIds) //默认选择的节点
        // menu.children && menu.children.forEach(item => this.$refs.menu.setChecked(item.id, true, true))
        // this.$refs.menu.setCheckedKeys([menu.id], true)
        // this.$refs.menu.setChecked(menu.id, true, true)
        // console.log(tree.checkedKeys)//
        // console.log(tree.checkedKeys.includes(menu.id))
        // console.log(this.$refs.menu.getCheckedKeys())
        let id = menu.id
        // 是否勾选
        let checkFlag = tree.checkedKeys.includes(menu.id)
        // 当前节点被选择，则以下子节点全被选择;当前节点被取消，则以下子节点全被取消
        menu.children && menu.children.forEach(item => this.childCheck(item, checkFlag))
        // 设置 选择的菜单节点
        this.selectMenuIds = [...this.$refs.menu.getCheckedKeys()];
      },
      childCheck(child, checkFlag) {
        this.$refs.menu.setChecked(child.id, checkFlag)
        if (child.children) {
          child.children.forEach(item => this.childCheck(item, checkFlag))
        }
      },
      // 保存菜单
      saveMenu() {
        let selectMenuIds = this.selectMenuIds
        if (!selectMenuIds || selectMenuIds.length < 1) {
          this.$message.warning('请选择节点！')
          return
        }
        this.menuLoading = true
        let params = {roleId: this.currentId, menuIds: [...selectMenuIds]}
        saveRoleMenus(params).then(() => {
          this.$message.success('保存成功')
          this.menuLoading = false
          this.refreshChange()
        }).catch(err => {
          this.menuLoading = false
          console.log(err.response.data.message)
        })
      },
    },
    mounted() {
      // 获取所有菜单数据
      allTreeList().then(res => {
        let list = res.data
        this.menus = list;
      });
    },
    created() {

    },
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
