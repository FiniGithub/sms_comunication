/**
 * jquery.bootstrap.js
 Copyright (c) Kris Zhang <kris.newghost@gmail.com>
 License: MIT (https://github.com/newghost/bootstrap-jquery-plugin/blob/master/LICENSE)
 Version: 0.0.2
 */

/* Extend string method */
/*
 string.format, ref: http://stackoverflow.com/questions/610406/javascript-equivalent-to-printf-string-format/4673436#4673436
 */
if (!String.prototype.format) {
    String.prototype.format = function () {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function (match, number) {
            return typeof args[number] != 'undefined'
                ? args[number]
                : match
                ;
        });
    };
}

/*
 Description: $.fn.dialog
 Author: Kris Zhang
 Dependence: N/A
 */
;(function ($) {

    $.fn.dialog = function (options) {

        var self = this
            , $this = $(self)
            , $body = $(document.body)
            , $msgbox = $this.closest('.dialog')
            , parentDataName = 'dialog-parent'
            , arg1 = arguments[1]
            , arg2 = arguments[2]
            ;

        var create = function () {

            var msghtml
                    = ''
                    + '<div class="dialog modal fade">'
                    + '<div class="modal-dialog">'
                    + '<div class="modal-content">'
                    + '<div class="modal-header">'
                    + '<button type="button" class="close">&times;</button>'
                    + '<h4 class="modal-title"></h4>'
                    + '</div>'
                    + '<div class="modal-body"></div>'
                    + '<div class="modal-footer"></div>'
                    + '</div>'
                    + '</div>'
                    + '</div>'
                ;


            $msgbox = $(msghtml);
            $(document.body).append($msgbox);
            $msgbox.find(".modal-body").append($this);
        };

        var createButton = function (_options) {
            var buttons = (_options || options || {}).buttons || {}
                , $btnrow = $msgbox.find(".modal-footer");

            //clear old buttons
            $btnrow.empty();

            var isButtonArr = buttons.constructor == Array;

            for (var button in buttons) {
                var btnObj = buttons[button]
                    , id = ""
                    , text = ""
                    , classed = "btn-default"
                    , click = "";

                if (btnObj.constructor == Object) {
                    id = btnObj.id;
                    text = btnObj.text;
                    classed = btnObj['class'] || btnObj.classed || classed;
                    click = btnObj.click;
                }

                //Buttons should be an object, etc: { 'close': function { } }
                else if (!isButtonArr && btnObj.constructor == Function) {
                    text = button;
                    click = btnObj;
                }

                else {
                    continue;
                }

                //<button data-bb-handler="danger" type="button" class="btn btn-danger">Danger!</button>
                $button = $('<button type="button" class="btn">').addClass(classed).html(text);

                id && $button.attr("id", id);
                if (click) {
                    (function (click) {
                        $button.click(function () {
                            click.call(self);
                        });
                    })(click);
                }

                $btnrow.append($button);
            }

            $btnrow.data('buttons', buttons);
        };

        var show = function () {
            // call the bootstrap modal to handle the show events (fade effects, body class and backdrop div)
            $msgbox.modal('show');
        };

        var close = function (destroy) {
            // call the bootstrap modal to handle the hide events and remove msgbox after the modal is hidden
            $msgbox.modal('hide').one('hidden.bs.modal', function () {
                if (destroy) {
                    $this.data(parentDataName).append($this);
                    $msgbox.remove();
                }
            });
        };

        if (options.constructor == Object) {
            !$this.data(parentDataName) && $this.data(parentDataName, $this.parent());

            if ($msgbox.size() < 1) {
                create();
            }
            createButton();
            $(".modal-title", $msgbox).html(options.title || "");
            var $modalDialog = $(".modal-dialog", $msgbox).addClass(options.dialogClass || "");
            $(".modal-header .close", $msgbox).click(function () {
                var closeHandler = options.onClose || close;
                closeHandler.call(self);
            });
            (options['class'] || options.classed) && $msgbox.addClass(options['class'] || options.classed);
            /*
             Passing the options, etc: backdrop, keyboard
             */
            options.autoOpen === false && (options.show = false)
            options.width && $modalDialog.width(options.width)
            options.height && $modalDialog.height(options.height)
            $msgbox.modal(options)
        }

        if (options == "destroy") {
            close(true);
        }

        if (options == "close") {
            close();
        }

        if (options == "open") {
            show();
        }

        if (options == "option") {
            if (arg1 == 'buttons') {
                if (arg2) {
                    createButton({buttons: arg2});
                    show();
                } else {
                    return $msgbox.find(".modal-footer").data('buttons');
                }
            }
        }

        return self;
    };

})(jQuery);


/*
 Description: $.messager
 Author: Kris Zhang
 Dependence: string.format.js $.fn.dialog
 */
$.messager = (function () {

    var alert = function (title, message) {
        var model = $.messager.model;

        if (arguments.length < 2) {
            message = title || "";
            title = "&nbsp;"
        }

        $("<div>" + message + "</div>").dialog({
            title: title
            // override destroy methods;
            , onClose: function () {
                $(this).dialog("destroy");
            }
            , buttons: [{
                text: model.ok.text
                , classed: model.ok.classed || "btn-success"
                , click: function () {
                    $(this).dialog("destroy");
                }
            }]
        });
    };

    var confirm = function (title, message, callback) {
        var model = $.messager.model;

        $("<div>" + message + "</div>").dialog({
            title: title
            // override destroy methods;
            , onClose: function () {
                $(this).dialog("destroy");
            }
            , buttons: [{
                text: model.ok.text
                , classed: model.ok.classed || "btn-success"
                , click: function () {
                    $(this).dialog("destroy");
                    callback && callback();
                }
            },
                {
                    text: model.cancel.text
                    , classed: model.cancel.classed || "btn-danger"
                    , click: function () {
                    $(this).dialog("destroy");
                }
                }]
        });
    };

    /*
     * popup message
     */
    var msghtml
            = ''
            + '<div class="dialog modal fade msg-popup">'
            + '<div class="modal-dialog modal-sm">'
            + '<div class="modal-content">'
            + '<div class="modal-body text-center"></div>'
            + '</div>'
            + '</div>'
            + '</div>'
        ;

    var $msgbox = $('.dialog.msg-popup')

    var popup = function (message) {
        if (!$msgbox.size()) {
            $msgbox = $(msghtml);
            $('body').append($msgbox);
        }

        $msgbox.find(".modal-body").html(message);
        $msgbox.modal({show: true, backdrop: false});

        setTimeout(function () {
            $msgbox.modal('hide');
        }, 2000);
    };

    return {
        alert: alert
        , popup: popup
        , confirm: confirm
    };

})();


$.messager.model = {
    ok: {text: "OK", classed: 'btn-success'},
    cancel: {text: "Cancel", classed: 'btn-danger'}
};

/*
 Description: $.fn.datagrid
 Author: Kris Zhang
 Dependence: string.format.js
 */
(function ($) {

    $.fn.datagrid = function (method, options) {

        var self = this
            , $this = $(self)
            , conf = $this.data("config") || {}
            , rows = $this.data("rows") || []
            , selectedClass = conf.selectedClass || "success"
            , singleSelect = conf.singleSelect
            ;

        var bindRows = function ($rows) {
            var selectChange = conf.selectChange
                , edit = conf.edit
                ;

            var selectHandler = function (e) {
                var $row = $(this)
                    , hasSelectedClass = $row.hasClass(selectedClass)
                    , idx = $("tbody tr", $this).index($row)
                    , row = rows[idx] || {}
                    ;

                //rows may added dynamiclly
                singleSelect && $("tbody tr", $this).removeClass(selectedClass);
                $row.toggleClass(selectedClass);

                //API selectChange: function( selected, rowIndex, rowData )
                selectChange && selectChange(!hasSelectedClass, idx, row, $row);
            };
            (selectChange || typeof singleSelect != "undefined") && $rows.click(selectHandler);


            var editHandler = function (e) {
                var $input = $(this)
                    , $row = $input.closest("tr")
                    , idx = $("tbody tr", $this).index($row)
                    , row = rows[idx] || {}
                    , name = $input.attr("name")
                    ;

                name && (row[name] = $input.val());
            }
            edit && $rows.find("input").keyup(editHandler);
        };

        var getRow = function (columns, row) {
            var trow = "<tr>";

            for (var j = 0, m = columns[0].length; j < m; j++) {
                var column = columns[0][j]
                    , format = column.formatter
                    , field = column.field
                    , tip = column.tip
                    , value = row[field]
                    , maxlength = column.maxlength
                    , readonly = column.readonly
                    ;

                typeof value == "undefined" && (value = "");

                if (conf.edit) {
                    maxlength = maxlength
                        ? ' maxlength="{0}"'.format(column.maxlength)
                        : '';

                    readonly = readonly ? ' readonly="readonly"' : '';

                    value
                        = '<input name="{0}" value="{1}" class="form-control"{2}{3}/>'.format(
                        column.field
                        , value
                        , maxlength
                        , readonly
                    );
                }

                //if it has 'formatter' attribute override the content
                value = format ? format(row[field], row) : value;
                trow = trow + "<td>" + value + "</td>";
            }
            ;
            trow += "</tr>";
            return trow;
        };

        var getData = function (edit) {
            if (!options) return;

            var columns = conf.columns;
            rows = options.rows || options;

            if (!columns) {
                return
            }

            var body = "<tbody>";
            if (rows) {
                for (var i = 0, l = rows.length; i < l; i++) {
                    body += getRow(columns, rows[i]);
                }
            }
            body += "</tbody>";

            $("tbody", $this).remove();
            $this
                .data("rows", rows)
                .append(body);

            //add "edit" class if it's edit mode.
            conf.edit && $this.addClass("edit");
            //rebind events
            bindRows($("tbody tr", $this));
        };

        var getSelectedIndex = function () {
            if (options && typeof options.index != "undefined") {
                return [options.index];
            } else {
                var selected = [];
                $this.find('tbody tr').each(function (index) {
                    var $tr = $(this);
                    $tr.hasClass(selectedClass) && selected.push(index)
                });
                return selected;
            }
        };

        //handle: $().datagrid({column: [[]]})
        if (method && method.constructor == Object) {
            var columns = method.columns;

            if (columns) {
                $("thead", $this).size() < 1
                && $this.append("<thead></thead>");

                var header = "<tr>";
                //method.del && (header += "<td></td>");
                for (var i = 0, l = columns[0].length; i < l; i++) {
                    var col = columns[0][i];
                    header += '<th>' + (col.title || "") + '</th>';
                }
                header += "</tr>";

                $this.data("config", method);
                $("thead", $this).html(header);
            }
        }

        //handle: $().datagrid("loadData", {rows: []}) or $().data("loadData", [])
        if (method == "loadData") getData();

        if (method == "getData") {
            return rows;
        }

        if (method == "getConfig") {
            return conf;
        }

        if (method == "getColumns") {
            return conf.columns;
        }

        if (method == "selectRow") {
            if (typeof singleSelect == "undefined") {
                return
            }

            if (typeof options == "number") {
                singleSelect && $this.datagrid('unselectRow');
                $("tbody tr", $this).eq(options).addClass(selectedClass);
            }

            else if (!singleSelect) {
                $("tbody tr", $this).addClass(selectedClass);
            }
        }

        if (method == "unselectRow") {
            typeof options != "undefined"
                ? $("tbody tr", $this).eq(options).removeClass(selectedClass)
                : $("tbody tr", $this).removeClass(selectedClass);
        }

        if (method == "updateRow") {
            var ids = getSelectedIndex()
                , row = options.row
                , columns = conf.columns
                ;

            for (var i = 0, l = ids.length; i < l; i++) {
                var id = ids[i];

                rows && (row = $.extend(rows[id], row));

                var $row = $(getRow(columns, row, conf));

                typeof options.index == "undefined" && $row.addClass(selectedClass);

                $("tbody tr", $this).eq(id)
                    .after($row)
                    .remove();

                bindRows($row);
            }
        }

        if (method == "getSelections") {
            var selRows = [];

            $("tbody tr", $this).each(function (idx) {
                $(this).hasClass(selectedClass) && selRows.push(rows[idx]);
            });

            return selRows;
        }

        if (method == "getSelectedIndex") {
            return getSelectedIndex();
        }

        if (method == "insertRow") {
            var idx = getSelectedIndex()[0]
                , row = options.row
                ;

            if (typeof idx == 'undefined' || idx < 0) {
                idx = rows.length
            }

            if (!conf || !row) return $this;

            var $rows = $("tbody tr", $this)
                , $row = $(getRow(conf.columns, row, conf))
                , $tar = $rows.eq(idx)
                ;

            bindRows($row);
            $tar.size() ? $tar.before($row) : $("tbody", $this).append($row);
            rows.splice(idx, 0, row);
        }

        if (method == "deleteRow") {
            var ids = typeof options == "number" ? [options] : getSelectedIndex();

            for (var i = ids.length - 1; i > -1; i--) {
                var idx = ids[i];
                $("tbody tr", $this).eq(idx).remove();
                rows.splice(idx, 1);
            }
        }

        return self;
    };


})(jQuery);

/*
 Description: $.fn.tree
 Author: Kris Zhang
 Dependence: string.format.js
 */
(function ($) {

    $.fn.tree = function (method, options) {

        var self = this
            , $this = $(self)
            , pushFn = Array.prototype.push
            , treeClass = 'nav'
            , activeClass = 'active'    /*on LI*/
            , selectedClass = 'selected'  /*on A*/
            , folderIcon = ''
            , itemIcon = ''
            , indentIcon = ''


        var build = function (data, indent) {
            var tree = [];

            tree.push('<ul class="{0} {1}">'.format(indent ? '' : 'tree-nav', treeClass))

            for (var i = 0, l = data.length; i < l; i++) {
                var node = data[i]
                    , nodes = node.nodes
                    , id = node.id
                    , active = node.active
                    , classed = node.classed
                    , attr = node.attr
                    , iconClass = node.icon || (nodes != null && nodes.length > 0 ? folderIcon : itemIcon)
                    , itemClass = node.itemClass || ''

                if(node.type == 'menu'){
                tree.push('<li class="{0} {1} {2}">'.format(active ? activeClass : '', nodes && nodes.length ? '' : 'no-child', itemClass))
                }
                var btn_nbsp = '';
                // 最后一排按钮缩进
                if(node.type == 'button' && i==0){
                    btn_nbsp = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
                }
                // 从第二个复选框开始不需要太多空格
                if(node.type == 'button' && i > 0){
                    indent='';
                }
                icon = btn_nbsp+'<i class="{0}"></i>'.format(nodes ? ('tree-folder ' + iconClass) : ('tree-item ' + iconClass))
                var item = '<a{1}{2} class="{3}" data-path="{5}" title="{4}">{0}<input type="checkbox" key="{6}" parent="{7}" inp_type="{8}" tops="{9}"/>&nbsp;<span>{4}</span></a>'.format(
                    indent + icon
                    , id ? " id='{0}'".format(id) : ""
                    , attr ? " data-attr='{0}'".format(JSON.stringify(attr)) : ""
                    , classed || ''
                    , node.text
                    , typeof node.path == 'undefined' ? node.text : node.path
                    ,node.id
                    ,node.parentId
                    ,node.type
                    ,node.topsId
                )
                tree.push(item)
                nodes && pushFn.apply(tree, build(nodes, indent + '<i class="tree-indent {0}"></i>'.format(indentIcon)))
                if(node.type == 'menu') {
                tree.push('</li>')
                }
            }

            tree.push('</ul>')

            return tree
        }

        /*
         If no parameter provided, using selected nodes or return the whole tree
         */
        var getCurrentNode = function ($node) {
            if (!$node) {
                $node = $this.find('.' + selectedClass)
            }

            if ($node.size() < 1) {
                $node = $this
            }

            if ($node[0].tagName != 'A') {
                $node = $node.find('>a')
            }

            return $node
        }

        var getParents = function ($node) {
            $node = getCurrentNode($node)

            var path = []
                , $parents = $node.parents()
                , size = $parents.size()

            for (var i = 0; i < size; i++) {
                var $parent = $parents.eq(i)

                if ($parent.hasClass('tree-nav')) {
                    return path
                }

                if ($parent[0].tagName == 'LI') {
                    var $link = $parent.find('>a')

                    path.push({
                        id: $link.attr("id") || ''
                        , text: $link.text()
                        , path: $link.data('path') || ''
                        , attr: JSON.parse($link.attr("data-attr") || '{}')
                    })
                }
            }

            return path
        }

        var getChildren = function ($node) {
            $node = getCurrentNode($node)

            var children = []

            $node.parent().find('>ul>li>a').each(function () {
                var $link = $(this)

                children.push({
                    id: $link.attr('id')
                    , path: $link.data('path')
                    , text: $link.text()
                    , attr: JSON.parse($link.attr('data-attr') || '{}')
                })
            })

            return children
        }

        var getPath = function (nodes) {
            var pathArr = []

            for (var i = nodes.length - 1; i > -1; i--) {
                var node = nodes[i]
                pathArr.push(node.path)
            }

            return pathArr.join('/')
        }

        var selectNode = function (path) {
            var $node
                , i

            if (typeof path == 'string') {
                var $path = []
                    , $link
                    , pathArr = path.split('/')
                    , pathStr

                $node = $this.find('>ul')

                for (i = 0; i < pathArr.length; i++) {
                    pathStr = pathArr[i]
                    if (pathStr) {
                        $link = $node.find('>li>[data-path={0}]'.format(pathStr))
                    } else {
                        $link = $node.find('>li>[data-path]').filter(function () {
                            return !$(this).data('path')
                        })
                    }

                    if ($link.size() < 1 || $link.data('path') != pathStr) {
                        return null
                    }
                    $node = $link.parent().find('>ul')
                }

                $node = $link.parent()
            } else {
                $node = path
            }

            $node = getCurrentNode($node)
            $node.click()

            var $parents = $node.parents('li')
                , size = $parents.size()

            for (i = 0; i < size; i++) {
                var $parent = $parents.eq(i)
                $parent.addClass(activeClass)
            }

            return $node
        }


        if (method && method.constructor == Object) {
            treeClass = method.treeClass || treeClass
            activeClass = method.activeClass || activeClass
            selectedClass = method.selectedClass || selectedClass
            folderIcon = typeof method.folderIcon == 'undefined' ? folderIcon : method.folderIcon
            itemIcon = typeof method.itemIcon == 'undefined' ? itemIcon : method.itemIcon
            indentIcon = typeof method.indentIcon == 'undefined' ? indentIcon : method.indentIcon

            var data = method.data
            if (data && (data.constructor == Array)) {
                var htmlArr = build(data, '')
                $this.html(htmlArr.join(''))
                $this.data("config", method)
            }

            var clickHandler = method.onClick

            $("li>a>i", $this).click(function () {
                var $link = $(this)
                    , $wrap = $link.closest('li')
                    , $prev = $this.find('a.' + selectedClass)
                    , attr = $link.attr("data-attr")

                $prev.removeClass(selectedClass)
                $wrap.toggleClass(activeClass)
                $link.addClass(selectedClass)

                // if (clickHandler) {
                //     var nodes = getParents($link)
                //         , path = getPath(nodes)
                //
                //     clickHandler.call(self, {
                //         id: $link.attr("id")
                //         , attr: attr ? JSON.parse(attr) : {}
                //         , text: $link.text()
                //         , nodes: nodes
                //         , path: path
                //     }, $link)
                // }
            });
            $("a>input", $this).click(function () {
                var checkVal = $(this).prop("checked");
                // 当前级
                var key = $(this).attr("key");
                //当前级的上级
                var parent = $(this).attr("parent");
                //当前级的下级
                var child = $("input[parent='" + key + "']").attr("key");

                //是菜单类型还是按钮类型
                var inp_type = $(this).attr("inp_type");

                //当前级的上级的上级
                var parent_parent_key = $("input[key='" + parent + "']").attr("parent");

                //菜单
                if (inp_type == 'menu') {
                    //选中
                    if (checkVal) {
                        //获取当前的所有下级
                        $("input[parent='" + key + "']").each(function () {
                            var key = $(this).attr("key");
                            //选中与取消状态
                            $("input[key='" + key + "']").prop("checked", checkVal);
                            $("input[parent='" + key + "']").prop("checked", checkVal);
                        });
                        //当前级的上级选中
                        $("input[key='" + parent + "']").prop("checked", checkVal);
                    }else{
                        // var number = $("input[parent='" + parent + "']:checked").length;
                        // //如果同一级选中的个数为0
                        // if(number == 0){
                        //     $("input[key='" + parent + "']").prop("checked", checkVal);
                        // }
                        //当前级取消
                        $("input[key='" + key + "']").prop("checked", checkVal);

                        var number = $("input[parent='" + parent + "']:checked").length;
                        if(number == 0){
                            //当前级 上级取消
                            $("input[key='" + parent + "']").prop("checked", checkVal);
                        }
                        //当前级 下级取消
                        $("input[parent='" + key + "']").prop("checked", checkVal);
                        //当前级 下级的下级取消
                        $("input[parent='" + child + "']").prop("checked", checkVal);

                    }
                }
                //按钮
                if (inp_type == 'button') {

                    //选中
                    if (checkVal) {
                        $("input[key='" + key + "']").prop("checked", checkVal);
                        $("input[key='" + parent + "']").prop("checked", checkVal);
                        $("input[key='" + parent_parent_key + "']").prop("checked", checkVal);
                    } else {
                        //获取父级为同一级的复选框的选中个数
                        var number = $("input[parent='" + parent + "']:checked").length;
                        if(number == 0){
                            //当上级取消选中
                            $("input[key='" + parent + "']").prop("checked", checkVal);
                        }
                        //当前选中的上级的上级
                        var parent_parent = $("input[key='" + parent + "']").attr("parent");
                        //当前选中的上级的上级 的下级的个数
                        var parent_number = $("input[parent='" + parent_parent + "']:checked").length;
                        if(parent_number == 0){
                            //将最上级取消
                            $("input[key='" + parent_parent + "']").prop("checked", checkVal);
                        }
                    }
                }



                /**/
                //当前级
                // var key = $(this).attr("key");
                // //下级
                // var chile_key = $("input[parent='"+key+"']").attr("key");
                //
                // //当前级上级
                // var parent_key = $(this).attr("parent");
                // //当前级上级上级
                // var parent_parent_key = $("input[key='"+parent_key+"']").attr("parent");
                //
                // //选中状态
                // if(checkVal){
                //     //下级用下下级选中
                //     $("input[parent='"+key+"']").prop("checked",true);
                //     $("input[parent='"+chile_key+"']").prop("checked",true);
                //     //上级及上上级也选中
                //     $("input[key='"+parent_key+"']").prop("checked",true);
                //     $("input[key='"+parent_parent_key+"']").prop("checked",true);
                //
                // }else{
                //     //下级用下下级不选中
                //     $("input[parent='"+key+"']").prop("checked",false);
                //     $("input[parent='"+chile_key+"']").prop("checked",false);
                //     //上级及上上级也不选中
                //     // 下级的选中个数
                //     var child_length =   $("input[parent='"+parent_key+"']:checked").length;
                //     if(child_length == 0){
                //         $("input[key='"+parent_key+"']").prop("checked",false);
                //         $("input[key='"+parent_parent_key+"']").prop("checked",false);
                //     }
                //
                // }
            });
        }

        else if (method == 'getChildren') {
            return getChildren(options)
        }

        else if (method == 'getParents') {
            return getParents(options)
        }

        else if (method == 'select') {
            return selectNode(options)
        }


        return self
    };

})(jQuery);