// var ChineseDistricts_city = {};
$(function () {
    load_data();
    $("#add_data").bind("click", function () {
        $("#add_data_div").modal("show");
        // $("#image_div").hide();
        $("#apk_name_id").val("");
        $("#apk_version").val("");
        $("#apk_description").val("");
        $(".fileinput-remove-button").trigger("click");
        $("#casing option:first").prop("selected", 'selected');
        $("#apk_casing_value").val("");
        $("#apk_casing_value").hide();
        $("#apk_upload_value").val("");
        $("#apk_upload_value").hide();
        $("#i_openState_value").val("");
        $("#i_openState_value").hide();
        
    });
    $("#save_data_btn").bind("click", function () {
        $("#upload_form").submit();
    });

    $("#btn_del").bind("click", function () {
        var param = {"id": pid};
        ajaxCall({
            url: "/plugin/from/delete.do",
            type: "get",
            data: param,
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    refresh_data();
                }else if(data["retCode"] == "000999"){
                	 $("#del").modal("hide");
                	alert("存在有引用当前通道源的通道无法删除！");
                }else if(data["retCode"] == "000001"){
                	$("#del").modal("hide");
                	alert("操作失败！");
                }
            },
            error: function () {
            },
            complete: function () {
            }
        });
    });
    
});



//刷新数据
function refresh_data() {
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order//排位命令（desc，asc）
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'pluginList.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,
    	pageSize:50,
        pageList:[50],
        pagination: true,
        paginationPreText:"上一页",
        paginationNextText:"下一页",
        paginationHAlign:"left",
formatShowingRows: function (pageFrom, pageTo, totalRows) {
        	 if(totalRows>50){
        		 $(".minlid").show();			
        		}else{
        			$(".minlid").hide(); 
        			
        		}
		 return '总共 ' + totalRows + ' 条记录';
        	 },
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            // {
            //     title: "bianhao",
            //     field: "select",
            //     align: "center",
            //     valign: "middle",
            //     checkbox: true
            // },
            {
                title: "id编号",
                field: "id",
                align: "center",
                valign: "middle"
               
            },
            {
                title: "名称",
                field: "name",
                align: "center",
                valign: "middle"
            },
            {
                title: "通道名称",
                field: "aisleName",
                align: "center",
                valign: "middle"
            },
            {
                title: "通道标识",
                field: "className",
                align: "center",
                valign: "middle"
            },
             {
                 title: "路径",
                 field: "path",
                 align: "center",
                 valign: "middle"
             },
            {
                title: "上传时间",
                field: "createTime",
                align: "center",
                valign: "middle",               
                formatter: function (value, row, index) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: "内容",
                field: "config",
                align: "left",
                valign: "middle"
            },
            {
                title: "描述",
                field: "intro",
                align: "left",
                valign: "middle"
            },
            {
                title: '操作',
                align: 'center',
                valign: "middle",
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id);
                }
            }
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr, id) {
    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/plugin/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if (btn_obj["actionUrls"] == user_path + 'merge.do') {
                $("#add_button").show();
                edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\');"><i class="fa fa-paste"></i> 删除</a> ';
            }
        }
    }
    html = edit_html + delete_html;
    return html;
}

function editData(id) {
    $(".fileinput-remove-button").trigger("click");
    $('#add_data_div').modal("show");
    load_category_data();
    ajaxCall({
        url: "/claim/formEdit.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#id_input").val(obj["id"]);
                $("#apk_name_id").val(obj["name"]);
                $("#apk_description").val(obj["description"]);
                $("#apk_casing_value").val(obj["casingName"]);
                $("#apk_casing_value").show();
                $("#apk_upload_value").val(obj["path"]);
                $("#apk_upload_value").show();
                var operState = obj["operState"];
                if(operState!=null && operState=="1"){
                	operState = "安装"
                }
                if(operState!=null && operState=="2"){
                	operState = "卸载"
                }
                if(operState!=null && operState=="3"){
                	operState = "禁用"
                }
                $("#i_openState_value").val(operState);
                $("#i_openState_value").show();
                
                
                /*if (obj["path"]) {
                    $(".file-preview-image").attr("src",'/' + obj["path"]);
                }*/
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}

var pid;
function deleteData(id) {
    $("#del_h4_title").html("确定删除");
    $("#del_p_title").html("确认删除?删除后将无法复原");
    $('#del').modal("show");
    pid = id;
}

//搜索的分类数据
function load_search_city() {
    var param = {};
    ajaxCall({
        url: "/pdistrict/queryRecursion.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                if (arr && arr.length > 0) {
                    var categroy_arr1 = new Array();
                    var categroy_arr2 = new Array();
                    var categroy_arr3 = new Array();
                    var categroy_arr4 = new Array();
                    var categroy_arr5 = new Array();
                    var re1 = /^[A-Ga-g]$/;
                    var re2 = /^[H-Kh-k]$/;
                    var re3 = /^[L-Sl-s]$/;
                    var re4 = /^[T-Zt-z]$/;
                    for (var i = 0; i < arr.length; i++) {
                        var obj = arr[i];
                        var data = {
                            "code": obj["id"],
                            "address": obj["districtName"]
                        };
                        if (re1.test(obj["firstPy"])) {
                            categroy_arr1.push(data);
                        }
                        else if (re2.test(obj["firstPy"])) {
                            categroy_arr2.push(data);
                        }
                        else if (re3.test(obj["firstPy"])) {
                            categroy_arr3.push(data);
                        }
                        else if (re4.test(obj["firstPy"])) {
                            categroy_arr4.push(data);
                        } else {
                            categroy_arr5.push(data);
                        }
                    }
                    for (var k = 0; k < categroy_arr5.length; k++) {
                        categroy_arr4.push(categroy_arr5[k]);
                    }
                    var html_data = {
                        'A-G': categroy_arr1,
                        'H-K': categroy_arr2,
                        'L-S': categroy_arr3,
                        'T-Z': categroy_arr4
                    }
                    ChineseDistricts_city["86"] = html_data;
                    //封装插件
                    factory_city(jQuery, ChineseDistricts_city);
                    var $citypicker8 = $('#city-picker8');
                    $('#reset_city').click(function () {
                        $citypicker8.citypicker('reset');
                    });
                    //重置方法
                    $('#reset_city').trigger("click");
                }
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });
}

function factory_city($, ChineseDistricts) {
    'use strict';
    if (typeof ChineseDistricts === 'undefined') {
        throw new Error('The file "city-picker.data.js" must be included first!');
    }
    var NAMESPACE = 'citypicker';
    var EVENT_CHANGE = 'change.' + NAMESPACE;
    var PROVINCE = 'province';
    var CITY = 'city';
    var DISTRICT = 'district';

    function CityPicker(element, options) {
        this.$element = $(element);
        this.$dropdown = null;
        this.options = $.extend({}, CityPicker.DEFAULTS, $.isPlainObject(options) && options);
        this.active = false;
        this.dems = [];
        this.needBlur = false;
        this.init();
    }

    CityPicker.prototype = {
        constructor: CityPicker,

        init: function () {

            this.defineDems();

            this.render();

            this.bind();

            this.active = true;
        },

        render: function () {
            // '<input class="placeholder" placeholder="' + placeholder + '"/>'
            var p = this.getPosition(),
                placeholder = this.$element.attr('placeholder') || this.options.placeholder,
                textspan = '<span id="city_key" class="city-picker-span" style="' +
                    this.getWidthStyle(p.width + 30) + 'height:' +
                    p.height + 'px;line-height:' + (p.height - 1) + 'px;">' +
                    (placeholder ? '<span class="placeholder">' + placeholder + '</span>' : '') +
                    '<span class="title"></span><div class="arrow"></div>' + '</span>',

                dropdown = '<div class="city-picker-dropdown" style="left:0px;top:100%;height:auto;' +
                    this.getWidthStyle(p.width, true) + '">' +
                    '<div class="city-select-wrap">' +
                    '<div class="city-select-tab">' +
                    '<a class="active" data-count="province" style="height:auto;">省</a>' +
                    (this.includeDem('city') ? '<a data-count="city">市</a>' : '') +
                    // (this.includeDem('district') ? '<a data-count="district">三级</a>' : '') + '</div>' +
                    '<div class="city-select-content">' +
                    '<div class="city-select province" data-count="province"></div>' +
                    (this.includeDem('city') ? '<div class="city-select city" data-count="city"></div>' : '') +
                    (this.includeDem('district') ? '<div class="city-select district" data-count="district"></div>' : '') +
                    '</div></div>';

            this.$element.addClass('city-picker-input');
            this.$textspan = $(textspan).insertAfter(this.$element);
            this.$dropdown = $(dropdown).insertAfter(this.$textspan);
            var $select = this.$dropdown.find('.city-select');

            // setup this.$province, this.$city and/or this.$district object
            $.each(this.dems, $.proxy(function (i, type) {
                this['$' + type] = $select.filter('.' + type + '');
            }, this));

            this.refresh();
        },

        refresh: function (force) {
            // clean the data-item for each $select
            var $select = this.$dropdown.find('.city-select');
            $select.each(function () {
                var class_val = $(this).attr("class");
                if (class_val == 'city-select city' || class_val == 'city-select district') {
                    $(this).html("");
                }
                // alert($(this).html());
            });
            $select.data('item', null);
            // parse value from value of the target $element
            var val = this.$element.val() || '';
            val = val.split('/');
            $.each(this.dems, $.proxy(function (i, type) {
                if (val[i] && i < val.length) {
                    this.options[type] = val[i];
                } else if (force) {
                    this.options[type] = '';
                }
                this.output(type);
            }, this));
            this.tab(PROVINCE);
            this.feedText();
            this.feedVal();
        },

        defineDems: function () {
            var stop = false;
            $.each([PROVINCE, CITY, DISTRICT], $.proxy(function (i, type) {
                if (!stop) {
                    this.dems.push(type);
                }
                if (type === this.options.level) {
                    stop = true;
                }
            }, this));
        },

        includeDem: function (type) {
            return $.inArray(type, this.dems) !== -1;
        },

        getPosition: function () {
            var p, h, w, s, pw;
            p = this.$element.position();
            s = this.getSize(this.$element);
            h = s.height;
            w = s.width;
            if (this.options.responsive) {
                pw = this.$element.offsetParent().width();
                if (pw) {
                    w = w / pw;
                    if (w > 0.99) {
                        w = 1;
                    }
                    w = w * 100 + '%';
                }
            }

            return {
                top: p.top || 0,
                left: p.left || 0,
                height: h,
                width: w
            };
        },

        getSize: function ($dom) {
            var $wrap, $clone, sizes;
            if (!$dom.is(':visible')) {
                $wrap = $("<div />").appendTo($("body"));
                $wrap.css({
                    "position": "absolute !important",
                    "visibility": "hidden !important",
                    "display": "block !important"
                });

                $clone = $dom.clone().appendTo($wrap);

                sizes = {
                    width: $clone.outerWidth(),
                    height: $clone.outerHeight()
                };

                $wrap.remove();
            } else {
                sizes = {
                    width: $dom.outerWidth(),
                    height: $dom.outerHeight()
                };
            }

            return sizes;
        },

        getWidthStyle: function (w, dropdown) {
            if (this.options.responsive && !$.isNumeric(w)) {
                return 'width:' + w + ';';
            } else {
                return 'width:' + (dropdown ? Math.max(320, w) : w) + 'px;';
            }
        },

        bind: function () {
            var $this = this;

            $(document).on('click', (this._mouteclick = function (e) {
                var $target = $(e.target);
                var $dropdown, $span, $input;
                if ($target.is('.city-picker-span')) {
                    $span = $target;
                } else if ($target.is('.city-picker-span *')) {
                    $span = $target.parents('.city-picker-span');
                }
                if ($target.is('.city-picker-input')) {
                    $input = $target;
                }
                if ($target.is('.city-picker-dropdown')) {
                    $dropdown = $target;
                } else if ($target.is('.city-picker-dropdown *')) {
                    $dropdown = $target.parents('.city-picker-dropdown');
                }
                if ((!$input && !$span && !$dropdown) ||
                    ($span && $span.get(0) !== $this.$textspan.get(0)) ||
                    ($input && $input.get(0) !== $this.$element.get(0)) ||
                    ($dropdown && $dropdown.get(0) !== $this.$dropdown.get(0))) {
                    $this.close(true);
                }

            }));

            this.$element.on('change', (this._changeElement = $.proxy(function () {
                this.close(true);
                this.refresh(true);
            }, this))).on('focus', (this._focusElement = $.proxy(function () {
                this.needBlur = true;
                this.open();
            }, this))).on('blur', (this._blurElement = $.proxy(function () {
                if (this.needBlur) {
                    this.needBlur = false;
                    this.close(true);
                }
            }, this)));

            this.$textspan.on('click', function (e) {
                var $target = $(e.target), type;
                $this.needBlur = false;
                if ($target.is('.select-item')) {
                    type = $target.data('count');
                    $this.open(type);
                } else {
                    if ($this.$dropdown.is(':visible')) {
                        $this.close();
                    } else {
                        $this.open();
                    }
                }
            }).on('mousedown', function () {
                $this.needBlur = false;
            });

            this.$dropdown.on('click', '.city-select a', function () {
                var $select = $(this).parents('.city-select');
                var $active = $select.find('a.active');
                var last = $select.next().length === 0;
                var this_html = $(this).html();
                $active.removeClass('active');
                $(this).addClass('active');
                if ($active.data('code') !== $(this).data('code')) {
                    $select.data('item', {
                        address: $(this).attr('title'), code: $(this).data('code')
                    });
                    $(this).trigger(EVENT_CHANGE);
                    $this.feedText(this_html);
                    $("#district_name").attr("flag", $(this).attr("data-code"));
                    $this.feedVal();
                    if (last) {
                        $this.close();
                    }
                }
                //刷新数据
                refresh_data();
            }).on('click', '.city-select-tab a', function () {
                if (!$(this).hasClass('active')) {
                    var type = $(this).data('count');
                    $this.tab(type);
                }
            }).on('mousedown', function () {
                $this.needBlur = false;
            });

            if (this.$province) {
                this.$province.on(EVENT_CHANGE, (this._changeProvince = $.proxy(function () {
                    this.output(CITY);
                    this.output(DISTRICT);
                    this.tab(CITY);
                }, this)));
            }

            if (this.$city) {
                this.$city.on(EVENT_CHANGE, (this._changeCity = $.proxy(function () {
                    this.output(DISTRICT);
                    this.tab(DISTRICT);
                }, this)));
            }
        },

        open: function (type) {
            type = type || PROVINCE;
            this.$dropdown.show();
            this.$textspan.addClass('open').addClass('focus');
            this.tab(type);
        },

        close: function (blur) {
            this.$dropdown.hide();
            this.$textspan.removeClass('open');
            if (blur) {
                this.$textspan.removeClass('focus');
            }
        },

        unbind: function () {

            $(document).off('click', this._mouteclick);

            this.$element.off('change', this._changeElement);
            this.$element.off('focus', this._focusElement);
            this.$element.off('blur', this._blurElement);

            this.$textspan.off('click');
            this.$textspan.off('mousedown');

            this.$dropdown.off('click');
            this.$dropdown.off('mousedown');

            if (this.$province) {
                this.$province.off(EVENT_CHANGE, this._changeProvince);
            }

            if (this.$city) {
                this.$city.off(EVENT_CHANGE, this._changeCity);
            }
        },

        getText: function (item_address) {
            var text = '';
            var tex_val = '';
            if (item_address) {
                tex_val = item_address;
            }
            text = '<input id="district_name" onclick="inpunt_click(2)"  readonly="readonly" placeholder="选择地区" style="width: 201px;height: 26px;border: 1px solid #ccc;color: black;cursor:pointer" type="text" value="' + tex_val + '" />';
            return text;
        },

        getPlaceHolder: function () {
            return this.$element.attr('placeholder') || this.options.placeholder;
        },

        feedText: function (item_address) {
            var text = this.getText(item_address);
            // var this_html = '<input id="categor_name" placeholder="选择分类" style="width: 201px;height: 26px;border: 1px solid #ccc;color: black;" type="text" value="' + item_address?item_address:'' + '"/>';
            // var text = this_html;
            // var $text_html = text;
            if (text) {
                this.$textspan.find('>.placeholder').hide();
                this.$textspan.find('>.title').html(text).show();
            } else {
                this.$textspan.find('>.placeholder').text(this.getPlaceHolder()).show();
                this.$textspan.find('>.title').html('').hide();
            }
        },

        getVal: function () {
            var text = '';
            this.$dropdown.find('.city-select')
                .each(function () {
                    var item = $(this).data('item');
                    if (item) {
                        text += ($(this).hasClass('province') ? '' : '/') + item.address;
                    }
                });
            // alert("111"+text);
            return text;
        },

        feedVal: function () {
            this.$element.val(this.getVal());
        },

        output: function (type) {

            var options = this.options;
            //var placeholders = this.placeholders;
            var $select = this['$' + type];
            var data = type === PROVINCE ? {} : [];
            var item;
            var districts;
            var code;
            var matched = null;
            var value;

            if (!$select || !$select.length) {
                return;
            }

            item = $select.data('item');

            value = (item ? item.address : null) || options[type];

            code = (
                type === PROVINCE ? 86 :
                    type === CITY ? this.$province && this.$province.find('.active').data('code') :
                        type === DISTRICT ? this.$city && this.$city.find('.active').data('code') : code
            );

            if (code == 86) {
                districts = $.isNumeric(code) ? ChineseDistricts[code] : null;

                if ($.isPlainObject(districts)) {
                    $.each(districts, function (code, address) {
                        var provs;
                        if (type === PROVINCE) {
                            provs = [];
                            for (var i = 0; i < address.length; i++) {
                                if (address[i].address === value) {
                                    matched = {
                                        code: address[i].code,
                                        address: address[i].address
                                    };
                                }
                                provs.push({
                                    code: address[i].code,
                                    address: address[i].address,
                                    selected: address[i].address === value
                                });
                            }
                            data[code] = provs;
                        } else {
                            if (address === value) {
                                matched = {
                                    code: code,
                                    address: address
                                };
                            }
                            data.push({
                                code: code,
                                address: address,
                                selected: address === value
                            });
                        }
                    });
                }

                $select.html(type === PROVINCE ? this.getProvinceList(data) :
                    this.getList(data, type));
                $select.data('item', matched);
            } else {
                $select.html("");
                district_click(code, $select, data, type, matched, this, value);
            }
        },

        getProvinceList: function (data) {
            var list = [],
                $this = this,
                simple = this.options.simple;

            $.each(data, function (i, n) {
                list.push('<dl class="clearfix">');
                list.push('<dt>' + i + '</dt><dd>');
                $.each(n, function (j, m) {
                    list.push(
                        '<a ' +
                        ' title="' + (m.address || '') + '"' +
                        ' data-code="' + (m.code || '') + '"' +
                        ' class="' +
                        (m.selected ? ' active' : '') +
                        '">' +
                        ( simple ? $this.simplize(m.address, PROVINCE) : m.address) +
                        '</a>');
                });
                list.push('</dd></dl>');
            });

            return list.join('');
        },

        getList: function (data, type) {
            var list = [],
                $this = this,
                simple = this.options.simple;
            list.push('<dl class="clearfix"><dd>');

            $.each(data, function (i, n) {
                list.push(
                    '<a ' +
                    ' title="' + (n.address || '') + '"' +
                    ' data-code="' + (n.code || '') + '"' +
                    ' class="' +
                    (n.selected ? ' active' : '') +
                    '">' +
                    ( simple ? $this.simplize(n.address, type) : n.address) +
                    '</a>');
            });
            list.push('</dd></dl>');

            return list.join('');
        },

        simplize: function (address, type) {
            address = address || '';
            if (type === PROVINCE) {
                return address.replace(/[省,市,自治区,壮族,回族,维吾尔]/g, '');
            } else if (type === CITY) {
                return address.replace(/[市,地区,回族,蒙古,苗族,白族,傣族,景颇族,藏族,彝族,壮族,傈僳族,布依族,侗族]/g, '')
                    .replace('哈萨克', '').replace('自治州', '').replace(/自治县/, '');
            } else if (type === DISTRICT) {
                return address.length > 2 ? address.replace(/[市,区,县,旗]/g, '') : address;
            }
        },

        tab: function (type) {
            var $selects = this.$dropdown.find('.city-select');
            var $tabs = this.$dropdown.find('.city-select-tab > a');
            var $select = this['$' + type];
            var $tab = this.$dropdown.find('.city-select-tab > a[data-count="' + type + '"]');
            if ($select) {
                $selects.hide();
                $select.show();
                $tabs.removeClass('active');
                $tab.addClass('active');
            }
        },

        reset: function () {
            this.$element.val(null).trigger('change');
            // load_search_category();
            $("input[class='placeholder']").val("");
            refresh_data();
        },

        destroy: function () {
            this.unbind();
            this.$element.removeData(NAMESPACE).removeClass('city-picker-input');
            this.$textspan.remove();
            this.$dropdown.remove();
        }
    };

    CityPicker.DEFAULTS = {
        simple: false,
        responsive: false,
        placeholder: '请选择分类',
        level: 'district',
        province: '',
        city: '',
        district: ''
    };

    CityPicker.setDefaults = function (options) {
        $.extend(CityPicker.DEFAULTS, options);
    };

    // Save the other citypicker
    CityPicker.other = $.fn.citypicker;

    // Register as jQuery plugin
    $.fn.citypicker = function (option) {
        var args = [].slice.call(arguments, 1);

        return this.each(function () {
            var $this = $(this);
            var data = $this.data(NAMESPACE);
            var options;
            var fn;

            if (!data) {
                if (/destroy/.test(option)) {
                    return;
                }

                options = $.extend({}, $this.data(), $.isPlainObject(option) && option);
                $this.data(NAMESPACE, (data = new CityPicker(this, options)));
            }

            if (typeof option === 'string' && $.isFunction(fn = data[option])) {
                fn.apply(data, args);
            }
        });
    };

    $.fn.citypicker.Constructor = CityPicker;
    $.fn.citypicker.setDefaults = CityPicker.setDefaults;

    // No conflict
    $.fn.citypicker.noConflict = function () {
        $.fn.citypicker = CityPicker.other;
        return this;
    };

    $(function () {
        $('[data-toggle="city-picker"]').citypicker();
    });
}


function district_click(id, select, data_val, type, matched, this_val, value_val) {
    //alert(11);
    if (!id) {
        return;
    }
    var PROVINCE = 'province';
    var param = {};
    // if(id=86){
    //     url = "/category/queryRecursion.do";
    // }else{
    var url = "/pdistrict/districtChildList.do?menuId=" + $("#menu_id").val();
    param["id"] = id;
    // }
    ajaxCall({
        url: url,
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                if (data["data"]) {
                    var arr = data["data"]["dataList"];
                    if (arr && arr.length > 0) {
                        var districts = "{";
                        for (var i = 0; i < arr.length; i++) {
                            var obj = arr[i];
                            if (i == arr.length - 1) {
                                districts += obj["id"] + ":'" + obj["districtName"] + "'";
                            } else {
                                districts += obj["id"] + ":'" + obj["districtName"] + "',";
                            }
                        }
                        districts += "}";
                        //由JSON字符串转换为JSON对象
                        var districts_val = eval('(' + districts + ')');
                        ;
                        // if ($.isPlainObject(districts)) {
                        $.each(districts_val, function (code, address) {
                            var provs;
                            if (type === PROVINCE) {
                                provs = [];
                                for (var i = 0; i < address.length; i++) {
                                    if (address[i].address === value_val) {
                                        matched = {
                                            code: address[i].code,
                                            address: address[i].address
                                        };
                                    }
                                    provs.push({
                                        code: address[i].code,
                                        address: address[i].address,
                                        selected: address[i].address === value_val
                                    });
                                }
                                data_val[code] = provs;
                            } else {
                                if (address === value_val) {
                                    matched = {
                                        code: code,
                                        address: address
                                    };
                                }
                                data_val.push({
                                    code: code,
                                    address: address,
                                    selected: address === value_val
                                });
                            }
                        });
                        // }
                        select.html(type === PROVINCE ? this_val.getProvinceList(data_val) :
                            this_val.getList(data_val, type));
                        select.data('item', matched);
                    }
                }
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });
}

function load_category_data() {
    // cxSelectApi.setOptions({
    //     data: dataCustom
    // });
    var param = {};
/*    ajaxCall({
        url: "/category/queryRecursion.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                $('#api_data').cxSelect({
                    selects: ['province', 'city', 'area'],
                    // required: true,
                    jsonName: 'serviceName',
                    jsonValue: 'id',
                    jsonSub: 'child',
                    data: arr
                });
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });*/

    ajaxCall({
        url: "/pdistrict/queryRecursion.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                $('#city_data').cxSelect({
                    selects: ['province', 'city', 'area'],
                    // required: true,
                    jsonName: 'districtName',
                    jsonValue: 'id',
                    jsonSub: 'child',
                    data: arr
                });
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });


    // $('#api_data').cxSelect({
    //     selects: ['province', 'city', 'area'],
    // // required: true,
    // jsonValue: 'v',
    // data: dataCustom
    // });
}


//加载子级地区
function load_district_child(t, sele_id) {
    var param = {};
    var id;
    if (sele_id == 'district_two') {
        id = $(t).find("option:selected").val();
    }
    if (sele_id == 'district_three') {
        id = $(t).find("option:selected").attr("id_val");
    }
    if (id) {
        param["id"] = id;
    } else {
        return;
    }
    ajaxCall({
        url: "/pdistrict/districtChildList.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                var _html = '<option value="请选择">请选择</option>';
                for (var i = 0; i < arr.length; i++) {
                    var obj = arr[i];
                    _html += '<option value="' + obj["id"] + '" id_val="' + obj["id"] + '">' + obj["districtName"] + '</option>';
                }
                ;
                $("#" + sele_id).html(_html).prop('disabled', false).css({
                    'display': '',
                    'visibility': ''
                });
            }
        },
        error: function () {
            // alert();

        },
        complete: function () {
            // alert(2);
        }
    });

}