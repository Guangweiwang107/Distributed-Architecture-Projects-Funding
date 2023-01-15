function generateTree() {
	$.ajax({
		"url" : "menu/get/whole/tree.json",
		"type" : "post",
		"dataType" : "json",
		"success" : function(response) {
			var result = response.result;
			if (result == "SUCCESS") {

				var setting = {
					"view" : {
						"addDiyDom" : myAddDiyDom,
						"addHoverDom" : myAddHoverDom,
						"removeHoverDom" : myRemoveHoverDom
					},
					"data" : {
						"key" : {
							"url" : "maomi"
						}
					}
				};

				var zNodes = response.data;

				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			}

			if (result == "FAILED") {
				layer.msg(response.message);
			}
		}
	});
}

function myRemoveHoverDom(treeId, treeNode) {

	var btnGroupId = treeNode.tId + "_btnGrp";

	$("#" + btnGroupId).remove();

}

function myAddHoverDom(treeId, treeNode) {

	var btnGroupId = treeNode.tId + "_btnGrp";

	if ($("#" + btnGroupId).length > 0) {
		return;
	}

	var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
	var removeBtn = "<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
	var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
	
	var level = treeNode.level;

	var btnHTML = "";

	if (level == 0) {
		btnHTML = addBtn;
	}

	if (level == 1) {
		btnHTML = addBtn + " " + editBtn;

		// 获取当前节点的子节点数量
		var length = treeNode.children.length;

		// 如果没有子节点，可以删除
		if (length == 0) {
			btnHTML = btnHTML + " " + removeBtn;
		}
	}
	
	if(level == 2) {
		btnHTML = editBtn + " " + removeBtn;
		
	}
	
	var anchorId = treeNode.tId + "_a";
	
	$("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>");
	
}

//修改默认的图标
function myAddDiyDom(treeId, treeNode) {
	
	// treeId是整个树形结构附着的ul标签的id
	console.log("treeId="+treeId);
	
	// 当前树形节点的全部的数据，包括从后端查询得到的Menu对象的全部属性
	console.log(treeNode);
	
	// zTree生成id的规则
	// 例子：treeDemo_7_ico
	// 解析：ul标签的id_当前节点的序号_功能
	// 提示：“ul标签的id_当前节点的序号”部分可以通过访问treeNode的tId属性得到
	// 根据id的生成规则拼接出来span标签的id
	var spanId = treeNode.tId + "_ico";
	
	// 根据控制图标的span标签的id找到这个span标签
	// 删除旧的class
	// 添加新的class
	$("#"+spanId)
		.removeClass()
		.addClass(treeNode.icon);
	
}