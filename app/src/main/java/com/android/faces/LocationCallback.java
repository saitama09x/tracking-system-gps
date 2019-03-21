package com.android.faces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.android.R;

public interface LocationCallback {
	void getLatLong(double lng, double lat);
}