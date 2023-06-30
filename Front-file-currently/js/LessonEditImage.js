
// 画像のプレビュー表示
$(function() {
    $('#image').on('change', function(e) {
        // 1枚だけ表示する
        var file = e.target.files[0];

        // ファイルリーダー作成
        var fileReader = new FileReader();
        fileReader.onload = function() {
            // Data URIを取得
            var dataUri = this.result;

            // img要素に表示
            $('#show-image').attr('src', dataUri);
        }

        // ファイルをData URIとして読み込む
        fileReader.readAsDataURL(file);
    });
});

// 画面の遷移チェック
function MoveCheck() {
    if( confirm("編集内容を破棄して遷移しますか？") ) {
        return true;    // リンクを発火させる
    }
    else {
        alert("遷移をキャンセルしました。");
        return false;   // リンクを発火させない
    }
}

// 削除チェック
function DeleteCheck() {
    if( confirm("該当の記事を削除してもよろしいでしょうか？") ) {
    }
    else {
        alert("削除をキャンセルしました。");
        return false;   // リンクを発火させない
    }
}
