/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights
 *          reserved. For licensing, see LICENSE.md or
 *          http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function (config) {
    config.filebrowserImageUploadUrl = "uploadImg.do"
    config.uiColor = '#F2F2F2';// 颜色
    config.resize_enabled = false;// 禁止拖动窗口
    config.height = 490; // 高度
    config.extraPlugins += (config.extraPlugins ? 'lineheight' : 'lineheight');// 行间距
    // 工具栏配置
   /* config.toolbar = [['Undo', 'Redo', 'Bold', 'Italic', "Image", "Format",
        "FontSize", "TextColor", "Link", "Unlink", "CodeSnippet","lineheight"]];*/
    config.toolbar ='Basic';
    //名字为“Basic”的toolbar（工具栏）的具体设定。只保留以下功能：
    config.toolbar_Basic =
        [
            { name: 'styles', items: ['Font', 'FontSize',"lineheight"] }, //样式栏：字体、大小、行间距
            {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'] }, //对齐栏：左对齐、中心对齐、右对齐、两端对齐
            {name: 'colors', items: ['TextColor', 'BGColor'] }, //颜色栏：文本颜色、背景颜色
            {name: 'basicstyles', items: ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat'] }, //基本样式栏：加粗、倾斜、下划线、删除线、下标、上标、移除样式
            {name: 'insert', items: ['Image', 'Table', 'HorizontalRule'] }, //插入栏：图像、flash、表格、水平线
            {name: 'links', items: ['Link', 'Unlink'] }, //超链接栏：增加超链接、取消超链接
            {name: 'document', items: ['Source',"Preview"]}//源代码栏：查看源代码、预览
        ];
};
