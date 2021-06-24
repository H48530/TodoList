var htmlRoot = document.querySelector("html");
var ulTodoList = document.querySelector("ul.toDoList");
var input = document.querySelector("input#todo");

function finishTodoItem() {
  // this 就是触发事件的元素（就是用户点击的元素)
  var id = this.dataset.id;
  var url = "/finish?id=" + id;
  var xhr = new XMLHttpRequest();
  var self = this;
  xhr.onload = function() {
    self.classList.add("finish");
    self.onclick = null;
  };
  xhr.open("get", url);
  xhr.send();
}

function appendTodo(todo) {
  var todoLi = document.createElement("li");
  todoLi.innerText = todo.待办事项;
  if (todo.是否完成 == true) {
    todoLi.classList.add("finish");
  } else {
    todoLi.onclick = finishTodoItem;

    todoLi.dataset.id = todo.id;
  }
  ulTodoList.appendChild(todoLi);
}

// 为 “submit” 按钮绑定 save 函数
function save() {
  var todoInput = input.value;
  var url = "/save?todo=" + encodeURIComponent(todoInput);
  var xhr = new XMLHttpRequest();
  function success() {
    var todo = {
      "id": parseInt(xhr.responseText),
      "待办事项": todoInput,
      "是否完成": 0
    };

    appendTodo(todo);
  }
  xhr.onload = success;
  xhr.open("get", url);
  xhr.send();
}

var submitBtn = document.querySelector(".button");
submitBtn.onclick = save;

// 页面加载完成之后，请求列表资源
function loadTodoList() {
  var xhr = new XMLHttpRequest();
  function success() {

    // 1. JS 进行 JSON 的反序列化
    var todoList = JSON.parse(xhr.responseText);
    // 2. 遍历每一项，添加到 DOM 树中
    for (var todo of todoList) {
      appendTodo(todo);
    }
  }
  xhr.onload = success;
  xhr.open("get", "/list");
  xhr.send();
}
loadTodoList();

// 点击未完成项时，请求完成资源