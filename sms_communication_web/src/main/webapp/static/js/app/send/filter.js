var path = $("#path").val();
$(function () {
    load_data();// 加载数据
    page();
    $("#search_btn").bind("click", function () {
//		getRealTimeOrSmsNum();
        refresh_data();
    });
});


//刷新数据
function refresh_data() {
//	getRealTimeOrSmsNum();
    $('#com-table').bootstrapTable('destroy');
    load_data();
}


/**
 * 加载数据
 */
function load_data() {
//	getRealTimeOrSmsNum();
    $('#com-table').bootstrapTable({
        url: 'filterList.do',
        dataType: "json",
        cache: false,
        striped: true,

        ccext: false,
        pageSize: 50,
        pageList: [50],
        pagination: true,
        paginationPreText: "上一页",
        paginationNextText: "下一页",
        paginationHAlign: "left",
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            if (totalRows > 50) {
                $(".minlid").show();
            } else {
                $(".minlid").hide();

            }
            return '总共 ' + totalRows + ' 条记录';
        },
        queryParams: queryParamsVal, //参数
        columns: [
            {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width: 100
            },
            /* {
             title: "项目",
             field: "project",
             align: "center",
             valign: "middle"
             },*/
            {
                title: "名称",
                field: "name",
                align: "center",
                valign: "middle",
                formatter: function (value, row, index) {
                    var str = row.name;
                    str = str.replace(/\/fileUpload\//g, "");
                    return str;
                }
            },
            {
                title: "时间",
                field: "time",
                align: "center",
                valign: "middle",
                width: 140,
                formatter: function (value, row, index) {
                    return new Date(row.time).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: "有效号码",
                field: "mobileNumber",
                align: "center",
                valign: "middle"
            },
            {
                title: "重复号码",
                field: "duplicateNumber",
                align: "center",
                valign: "middle"
            },
            {
                title: "错误号码",
                field: "wrongNumber",
                align: "center",
                valign: "middle"
            }
        ],
        formatNoMatches: function () {


            return '无符合条件的记录';
        }
    });
}


function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        menuId: $("#id_input").val(),
        email: $("#email").val()
    };
    return temp;
}