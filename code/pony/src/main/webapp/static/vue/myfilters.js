//depend on moment.js
Vue.filter('moment', function (value, formatString) {
    formatString = formatString || 'YYYY-MM-DD HH:mm:ss';
    return moment(value).format(formatString);
});
Vue.filter('date', function (value, formatString) {
    formatString = formatString || 'YYYY-MM-DD';
    return moment(value).format(formatString);
});
Vue.filter('time', function (value, formatString) {
    formatString = formatString || 'HH:mm:ss';
    return moment(value).format(formatString);
});
Vue.filter('hhmm', function (value, formatString) {
    formatString = formatString || 'HH:mm';
    return moment(value).format(formatString);
});