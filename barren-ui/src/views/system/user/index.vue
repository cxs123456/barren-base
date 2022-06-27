<template>
  <div class="app-container">
    user管理
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'User',
  components: {},

  data() {
    return {}
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  created() {

  },
  mounted: function() {

  },
  methods: {}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
