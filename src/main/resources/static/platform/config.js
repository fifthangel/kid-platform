layui.define(function(exports) {
  exports('conf', {
    container: 'platform',
    containerBody: 'platform-body',
    v: '2.0',
    base: layui.cache.base,
    css: layui.cache.base + 'css/',
    views: layui.cache.base + 'views/',
    viewLoadBar: true,
    debug: layui.cache.debug,
    name: 'platform',
    entry: '/index',
    engine: '',
    eventName: 'platform-event',
    tableName: 'platform',
    requestUrl: './'
  })
});
