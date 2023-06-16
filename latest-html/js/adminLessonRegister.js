
        // 获取文件选择输入框元素
        var fileInput = document.getElementById('upload-input');
      
        // 获取预览容器元素
        var previewContainer = document.getElementById('preview-container');
      
        // 获取预览图像元素
        var previewImage = document.getElementById('preview-image');
      
        // 获取占位文本元素
        var placeholderText = document.getElementById('placeholder-text');
      
        // 监听文件选择事件
        fileInput.addEventListener('change', function(event) {
          // 获取用户选择的文件
          var file = event.target.files[0];
      
          // 创建文件读取器
          var reader = new FileReader();
      
          // 监听文件读取完成事件
          reader.onload = function(e) {
            // 显示预览图像
            previewImage.src = e.target.result;
      
            // 隐藏占位文本
            placeholderText.style.display = 'none';
          };
      
          // 读取文件内容
          reader.readAsDataURL(file);
        });

    