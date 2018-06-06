package com.ichi2.anki.ankigame.features.deckpicker;

import android.support.v4.app.ActivityCompat;

import com.ichi2.anki.StudyOptionsFragment;
import com.ichi2.anki.ankigame.base.MvpView;
import com.ichi2.anki.ankigame.features.customstudy.CustomStudyDialog;
import com.ichi2.anki.dialogs.ExportDialog;
import com.ichi2.anki.dialogs.ImportDialog;
import com.ichi2.anki.dialogs.MediaCheckDialog;
import com.ichi2.anki.dialogs.SyncErrorDialog;

public interface DeckPickerMvpView extends MvpView,
        StudyOptionsFragment.StudyOptionsListener, SyncErrorDialog.SyncErrorDialogListener, ImportDialog.ImportDialogListener,
        MediaCheckDialog.MediaCheckDialogListener, ExportDialog.ExportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback, CustomStudyDialog.CustomStudyListener {
}
