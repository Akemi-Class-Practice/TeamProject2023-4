function showNotification(message) {
  // 通知要素を取得
  var notification = document.getElementById('notification');
  // 通知メッセージ要素を取得
  var notificationMessage = document.getElementById('notification-message');
  // 通知を閉じるボタン要素を取得
  var notificationCloseButton = document.getElementById('notification-close-button');

  // 通知メッセージを設定
  notificationMessage.innerText = message;
  // 通知を表示
  notification.style.display = 'block';

  // 通知を閉じるボタンのクリックイベントリスナーを追加
  notificationCloseButton.addEventListener('click', function () {
    // 通知を非表示にする
    notification.style.display = 'none';
  });
}

function hideNotification() {
  // 通知要素を取得
  var notification = document.getElementById('notification');
  // 通知を非表示にする
  notification.style.display = 'none';
}

function validateInputs() {
  // Eメール入力要素を取得
  var emailInput = document.getElementById('input1');
  // パスワード入力要素を取得
  var passwordInput = document.getElementById('input2');
  // パスワード確認入力要素を取得
  var confirmPasswordInput = document.getElementById('input3');

  // 入力値を取得
  var email = emailInput.value;
  var password = passwordInput.value;
  var confirmPassword = confirmPasswordInput.value;

  // 入力が空白の場合、通知を表示
  if (email.trim() === '' || password.trim() === '' || confirmPassword.trim() === '') {
    showNotification('入力してない内容があります！'); // 入力内容がありません！
  }
  // パスワードが一致しない場合、通知を表示
  else if (password !== confirmPassword) {
    showNotification('パスワードが一致しません！'); // パスワードが一致しません！
  }
  // 入力が正常な場合、通知を非表示にする
  else {
    hideNotification();
  }
}

// 通知要素を受け取る
const notification = document.getElementById('notification');

// マウスで長押すと通知ボックスはマウスと一緒に動く
let isDragging = false;
let dragOffsetX = 0;
let dragOffsetY = 0;

// マウスボタンが押された時にドラッグを開始する
notification.addEventListener('mousedown', function (event) {
  isDragging = true;
  dragOffsetX = event.clientX - notification.offsetLeft;
  dragOffsetY = event.clientY - notification.offsetTop;
});

// マウス移動時に通知ボックスの位置を更新する
document.addEventListener('mousemove', function (event) {
  if (isDragging) {
    const left = event.clientX - dragOffsetX;
    const top = event.clientY - dragOffsetY;
    notification.style.left = left + 'px';
    notification.style.top = top + 'px';
  }
});

// マウスボタンが離された時にドラッグを停止する
document.addEventListener('mouseup', function () {
  isDragging = false;
});