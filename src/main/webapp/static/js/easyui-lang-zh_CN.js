if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = '第';
	$.fn.pagination.defaults.afterPageText = '共{pages}页';
	$.fn.pagination.defaults.displayMsg = '显示{from}到{to},共{total}记录';
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = '正在处理，请稍待。。。';
}
if ($.fn.treegrid && $.fn.datagrid){
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager){
	$.messager.defaults.ok = '确定';
	$.messager.defaults.cancel = '取消';
}
if ($.fn.validatebox){
	$.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
	$.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
	$.fn.validatebox.defaults.rules.length.message = '输入内容长度必须介于{0}和{1}之间';
	$.fn.validatebox.defaults.rules.remote.message = '请修正该字段';
}
if ($.fn.numberbox){
	$.fn.numberbox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combobox){
	$.fn.combobox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combotree){
	$.fn.combotree.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combogrid){
	$.fn.combogrid.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['日','一','二','三','四','五','六'];
	$.fn.calendar.defaults.months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = '今天';
	$.fn.datebox.defaults.closeText = '关闭';
	$.fn.datebox.defaults.okText = '确定';
	$.fn.datebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.datebox.defaults.formatter = function(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	};
	$.fn.datebox.defaults.parser = function(s){
		if (!s) return new Date();
		var ss = s.split('-');
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	};
}
if ($.fn.datetimebox && $.fn.datebox){
	$.extend($.fn.datetimebox.defaults,{
		currentText: $.fn.datebox.defaults.currentText,
		closeText: $.fn.datebox.defaults.closeText,
		okText: $.fn.datebox.defaults.okText,
		missingMessage: $.fn.datebox.defaults.missingMessage
	});
}
if ($.fn.datetimespinner){
	$.fn.datetimespinner.defaults.selections = [[0,4],[5,7],[8,10],[11,13],[14,16],[17,19]];
}

//扩展验证
$.extend($.fn.validatebox.defaults.rules, {
    CHS : {
		validator : function(value, param) {
			return /^[\u0391-\uFFE5]+$/.test(value);
		},
		message : '请输入汉字'
	},
	english : {
		validator : function(value) {
			return /^[A-Za-z]+$/i.test(value);
		},
		message : '请输入英文'
	},
	ip : {
		validator : function(value) {
			return /\d+\.\d+\.\d+\.\d+/.test(value);
		},
		message : 'IP地址格式不正确'
	},
	ZIP : {
		validator : function(value, param) {
			return /^[0-9]\d{5}$/.test(value);
		},
		message : '邮政编码不存在'
	},
	QQ : {
		validator : function(value, param) {
			return /^[1-9]\d{4,10}$/.test(value);
		},
		message : 'QQ号码不正确'
	},
	mobile : {
		validator : function(value, param) {
			return /^(?:13\d|15\d|18\d)-?\d{5}(\d{3}|\*{3})$/
					.test(value);
		},
		message : '手机号码不正确'
	},
	tel : {
		validator : function(value, param) {
			return /^(\d{3}-|\d{4}-)?(\d{8}|\d{7})?(-\d{1,6})?$/
					.test(value);
		},
		message : '电话号码不正确'
	},
	mobileAndTel : {
		validator : function(value, param) {
			return /(^([0\+]\d{2,3})\d{3,4}\-\d{3,8}$)|(^([0\+]\d{2,3})\d{3,4}\d{3,8}$)|(^([0\+]\d{2,3}){0,1}13\d{9}$)|(^\d{3,4}\d{3,8}$)|(^\d{3,4}\-\d{3,8}$)/
					.test(value);
		},
		message : '请正确输入电话号码'
	},
	number : {
		validator : function(value, param) {
			return /^[0-9]+.?[0-9]*$/.test(value);
		},
		message : '请输入数字'
	},
	money : {
		validator : function(value, param) {
			return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/)
					.test(value);
		},
		message : '请输入正确的金额'

	},
	mone : {
		validator : function(value, param) {
			return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/)
					.test(value);
		},
		message : '请输入整数或小数'

	},
	integer : {
		validator : function(value, param) {
			return /^[+]?[1-9]\d*$/.test(value);
		},
		message : '请输入最小为1的整数'
	},
	integ : {
		validator : function(value, param) {
			return /^[+]?[0-9]\d*$/.test(value);
		},
		message : '请输入整数'
	},
	range : {
		validator : function(value, param) {
			if (/^[1-9]\d*$/.test(value)) {
				return value >= param[0] && value <= param[1];
			} else {
				return false;
			}
		},
		message : '输入的数字在{0}到{1}之间'
	},
	minLength : {
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : '至少输入{0}个字'
	},
	maxLength : {
		validator : function(value, param) {
			return value.length <= param[0];
		},
		message : '最多{0}个字'
	},
	selectValid : {
		validator : function(value, param) {
			if (value == param[0]) {
				return false;
			} else {
				return true;
			}
		},
		message : '请选择'
	},
	loginName : {
		validator : function(value, param) {
			return /^[\u0391-\uFFE5\w]+$/.test(value);
		},
		message : '登录名称只允许汉字、英文字母、数字及下划线。'
	},
	equalTo : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '两次输入的字符不一至'
	},
	englishOrNum : {
		validator : function(value) {
			return /^[a-zA-Z0-9_ ]{1,}$/.test(value);
		},
		message : '请输入英文、数字、下划线或者空格'
	},
	xiaoshu : {
		validator : function(value) {
			return /^(([1-9]+)|([0-9]+\.[0-9]{1,2}))$/
					.test(value);
		},
		message : '最多保留两位小数！'
	},
	ddPrice : {
		validator : function(value, param) {
			if (/^[1-9]\d*$/.test(value)) {
				return value >= param[0] && value <= param[1];
			} else {
				return false;
			}
		},
		message : '请输入1到100之间正整数'
	},
	jretailUpperLimit : {
		validator : function(value, param) {
			if (/^[0-9]+([.]{1}[0-9]{1,2})?$/.test(value)) {
				return parseFloat(value) > parseFloat(param[0])
						&& parseFloat(value) <= parseFloat(param[1]);
			} else {
				return false;
			}
		},
		message : '请输入0到100之间的最多俩位小数的数字'
	},
	rateCheck : {
		validator : function(value, param) {
			if (/^[0-9]+([.]{1}[0-9]{1,2})?$/.test(value)) {
				return parseFloat(value) > parseFloat(param[0])
						&& parseFloat(value) <= parseFloat(param[1]);
			} else {
				return false;
			}
		},
		message : '请输入0到1000之间的最多俩位小数的数字'
	},idcared: {     
        validator: function(value,param){    
            var flag= isCardID(value);  
            return flag==true?true:false;    
        },     
        message: '不是有效的身份证号码'    
    }
});

//身份证验证
var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};   

function isCardID(suspectidentityid){   
    var iSum=0 ;  
    if(!/^\d{17}(\d|x)$/i.test(suspectidentityid)) return "你输入的身份证长度或格式错误";   
    suspectidentityid=suspectidentityid.replace(/x$/i,"a");   
    if(aCity[parseInt(suspectidentityid.substr(0,2))]==null) return "你的身份证地区非法";   
    sBirthday=suspectidentityid.substr(6,4)+"-"+Number(suspectidentityid.substr(10,2))+"-"+Number(suspectidentityid.substr(12,2));   
    var d=new Date(sBirthday.replace(/-/g,"/")) ;  
    if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return "身份证上的出生日期非法";   
    for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(suspectidentityid.charAt(17 - i),11) ;  
    if(iSum%11!=1) return "你输入的身份证号非法";   
    return true; 
}   