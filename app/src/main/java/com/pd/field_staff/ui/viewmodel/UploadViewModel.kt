package com.pd.field_staff.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class UploadViewModel: ViewModel() {


    var fileList: MutableStateFlow<List<FileItem>> = MutableStateFlow<List<FileItem>>(emptyList())
        private set

    fun addFiles(files: List<FileItem>) {
        fileList.value += files
    }

    fun addFile(file: FileItem) {
        fileList.value += file
    }

}

data class FileItem(
    val id: String,
    val uri: Uri,
    val name: String,
    var isUploading: Boolean = false
)