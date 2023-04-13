package com.example.intellilearnteacherapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.intellilearnteacherapp.models.ClassModel
import kotlinx.android.synthetic.main.activity_automated_student_attendance.*
import kotlinx.android.synthetic.main.activity_my_attendance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService

class AutomatedStudentAttendance : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private val MY_PERMISSIONS_REQUEST_BLUETOOTH = 123

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automated_student_attendance)

        //request explicit bluetooth permissions
        checkAndRequestBluetoothPermissions()


        spinner = findViewById(R.id.drop_select_class_for_attendance)

        //first get all the classes taught by the teacher
        //set the values for the spinner after fetching data
        val classes: ArrayList<String> = ArrayList()

        MyApp.getInstance().getApiServices().getTeacherClasses(SharedPrefManager.getInstance(applicationContext).teacher.teacher_ID).enqueue(object :
            Callback<List<ClassModel>> {

            override fun onResponse(call: Call<List<ClassModel>>, response: Response<List<ClassModel>>) {

                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    //now extract data
                    val classesList: List<ClassModel>? = response.body()

                    if (classesList != null) {
                        for (classDesc in classesList) {

                            classes.add("Class ".plus(classDesc.class_level).plus(" Section ").plus(classDesc.section).plus(" ").plus(classDesc.subject))

                        }
                    }
                    // Update spinner on the main thread
                    //withContext(Dispatchers.Main) {
                    updateSpinner(classes)
                    //}

                }
                else{

                    Toast.makeText(this@AutomatedStudentAttendance, "Error loading Classes", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<ClassModel>>, t: Throwable) {

                Toast.makeText(this@AutomatedStudentAttendance, "Error loading Classes data", Toast.LENGTH_LONG).show()

            }


        })

        btnScan.setOnClickListener{

            //TODO:
            //code to scan the bluetooth MAC addresses
            //update the table afterwards

            //obtain an instance of the BluetoothAdapter class, which allows you to interact with the Bluetooth hardware on the device
            val context:Context = this@AutomatedStudentAttendance.applicationContext
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter

            //check if Bluetooth is enabled on the device, and if not, request that the user enable it
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@setOnClickListener
                }
                startActivity(enableBtIntent)
            }

            // Add the location permission check here
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission is required for Bluetooth device discovery", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //scan for devices
            // BroadcastReceiver for devices found
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = intent?.action
                    if (BluetoothDevice.ACTION_FOUND == action) {
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        if (ActivityCompat.checkSelfPermission(
                                this@AutomatedStudentAttendance,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        } else {
                            val deviceName = device?.name
                            val deviceHardwareAddress = device?.address // MAC address

                            Log.d("Bluetooth", "Found device: $deviceName $deviceHardwareAddress")
                        }
                    }
                }

            }

            // BroadcastReceiver for discovery finished
            val discoveryFinishedReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = intent?.action
                    if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                        unregisterReceiver(receiver)
                        unregisterReceiver(this) // Unregister discoveryFinishedReceiver
                    }
                }
            }

            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)

            val discoveryFinishedFilter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            registerReceiver(discoveryFinishedReceiver, discoveryFinishedFilter)

            bluetoothAdapter.startDiscovery()
            Log.d("Bluetooth", "Discovery started")


        }

    }

    private fun updateSpinner(optionsList: List<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            optionsList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSIONS_REQUEST_BLUETOOTH) {
            var bluetoothPermissionGranted = false
            var bluetoothAdminPermissionGranted = false
            var fineLocationPermissionGranted = false

            for (i in grantResults.indices) {
                when (permissions[i]) {
                    Manifest.permission.BLUETOOTH -> bluetoothPermissionGranted =
                        grantResults[i] == PackageManager.PERMISSION_GRANTED
                    Manifest.permission.BLUETOOTH_ADMIN -> bluetoothAdminPermissionGranted =
                        grantResults[i] == PackageManager.PERMISSION_GRANTED
                    Manifest.permission.ACCESS_FINE_LOCATION -> fineLocationPermissionGranted =
                        grantResults[i] == PackageManager.PERMISSION_GRANTED
                }
            }

            if (bluetoothPermissionGranted && bluetoothAdminPermissionGranted && fineLocationPermissionGranted) {
                // Permission granted, start scanning for Bluetooth devices
                //startBluetoothScan()
            } else {
                // Permission denied, show a message or disable Bluetooth features
                Toast.makeText(this, "Bluetooth and/or location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndRequestBluetoothPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            //Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permissionsToRequest = mutableListOf<String>()
        permissions.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(it)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(), MY_PERMISSIONS_REQUEST_BLUETOOTH)
        } else {
            // All Bluetooth permissions are already granted, start Bluetooth discovery
        }
    }

}