<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('permission:add')" @click="showCreate">添加</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="权限id" prop="id"></el-table-column>
      <el-table-column align="center" label="菜单编号" prop="menuCode"></el-table-column>
      <el-table-column align="center" label="菜单名称" prop="menuName"></el-table-column>
      <el-table-column align="center" label="权限代码" prop="permissionCode"></el-table-column>
      <el-table-column align="center" label="权限名称" prop="permissionName"></el-table-column>
      <el-table-column align="center" label="管理">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" v-if="hasPerm('permission:update')" @click="showUpdate(scope.$index)">
            修改
          </el-button>
          <el-button type="danger" icon="delete" v-if="hasPerm('permission:delete')"
                     @click="deletePermission(scope.$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempPermission" label-position="left" label-width="80px"
               style="width: 300px; margin-left: 50px;">
        <el-form-item label="权限id" required v-if="isCreate">
          <el-input type="text" v-model="tempPermission.id"></el-input>
        </el-form-item>
        <el-form-item label="菜单编号" required>
          <el-input type="text" v-model="tempPermission.menuCode"></el-input>
        </el-form-item>
        <el-form-item label="菜单名称" required>
          <el-input type="text" v-model="tempPermission.menuName"></el-input>
        </el-form-item>
        <el-form-item label="权限代码" required>
          <el-input type="text" v-model="tempPermission.permissionCode"></el-input>
        </el-form-item>
        <el-form-item label="权限名称" required>
          <el-input type="text" v-model="tempPermission.permissionName"></el-input>
        </el-form-item>
        <el-form-item label="必选权限" required>
          <el-radio v-model="tempPermission.requiredPermission" label="1">是</el-radio>
          <el-radio v-model="tempPermission.requiredPermission" label="2">否</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="isCreate" type="success" @click="createPermission">创 建</el-button>
        <el-button type="primary" v-else @click="updatePermission">修 改</el-button>
      </div>
    </el-dialog>
  </div>

</template>
<script>
  export default {
    data() {
      return {
        list: [],// 表格数据
        listLoading: false,// 数据加载等待动画
        totalCount: 0,// 分页组件--数据总条数
        listQuery: {
          pageNum: 1, // 页码
          pageRow: 10 // 每页条数
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          udapte: '编辑',
          create: '新建'
        },
        tempPermission: {
          id: '',
          menuCode: '',
          menuName: '',
          permissionCode: '',
          permissionName: '',
          requiredPermission: ''
        }
      };
    },
    created() {
      this.getList();
    },
    computed: {
      isCreate() {
        return this.dialogStatus === 'create';
      }
    },
    methods: {
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1;
      },
      getList() {
        // 查询列表
        this.listLoading = true;
        this.api({
          url: '/permission/list',
          method: 'get',
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        });
      },
      handleSizeChange(val) {
        // 改变每页数量
        this.listQuery.pageRow = val;
        this.listQuery.pageNum = 1
        this.getList();
      },
      handleCurrentChange(val) {
        // 改变页码
        this.listQuery.pageNum = val;
        this.getList();
      },
      showCreate() {
        // 显示新增对话框
        this.tempPermission.id = '';
        this.tempPermission.menuCode = '';
        this.tempPermission.menuName = '';
        this.tempPermission.permissionCode = '';
        this.tempPermission.permissionName = '';
        this.tempPermission.requiredPermission = '2';
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },
      showUpdate($index) {
        let permission = this.list[$index];
        // 显示修改对话框
        this.tempPermission.id = permission.id;
        this.tempPermission.menuCode = permission.menuCode;
        this.tempPermission.menuName = permission.menuName;
        this.tempPermission.permissionCode = permission.permissionCode;
        this.tempPermission.permissionName = permission.permissionName;
        this.tempPermission.requiredPermission = permission.requiredPermission + '';
        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
      },
      createPermission() {
        // 添加新权限
        this.api({
          url: '/permission/add',
          method: 'post',
          data: this.tempPermission
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false;
        });
      },
      updatePermission() {
        let _vue = this;
        this.api({
          url: '/permission/update',
          method: 'post',
          data: this.tempPermission
        }).then(() => {
          let msg = '修改成功';
          this.dialogFormVisible = false;
          this.$message({
            message: msg,
            type: 'success',
            duration: 1000,
            onClose: () => {
              _vue.getList();
            }
          });
        });
      },
      deletePermission($index) {
        let _vue = this;
        this.$confirm('确定删除此权限?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let permission = _vue.list[$index];
          _vue.api({
            url: '/permission/delete',
            method: 'post',
            data: {id: permission.id}
          }).then(() => {
            _vue.getList();
          });
        });
      }
    }
  }
</script>
