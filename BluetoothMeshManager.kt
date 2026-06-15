package com.pandora.mesh

import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BtMeshState(
    val advertising: Boolean = false,
    val scanning: Boolean = false,
    val peers: List<String> = emptyList(),
    val messagesSent: Int = 0,
    val messagesReceived: Int = 0,
)https://github.com/Hack-with-Github/Awesome-Hacking/tree/master/.github/workflows https://github.com/Hack-with-Github/Awesome-Hacking https://github.com/advisories/GHSA-2qrg-x229-3v8q 

class BluetoothMeshManager(private val context: Context) {

    private val _state = MutableStateFlow(BtMeshState())
    val state: StateFlow<BtMeshState> = _state

    private val PANDORA_UUID = UUID.fromString("0000CAFE-0000-1000-8000-00805F9B34FB")
    private val PANDORA_CHAR  = UUID.fromString("0000DEAD-0000-1000-8000-00805F9B34FB")

    private val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    private val btAdapter = btManager?.adapter
    private var advertiser: BluetoothLeAdvertiser? = null
    private var scanner: BluetoothLeScanner? = null
    private var gattServer: BluetoothGattServer? = null
    private val discoveredPeers = mutableSetOf<String>()

    fun initialize() {
        Log.i("BtMesh", "Bluetooth Mesh wird initialisiert...")
        if (btAdapter?.isEnabled == true) {
            startGattServer()
        }
    }

    fun startAdvertising(nodeId: String) {
        advertiser = btAdapter?.bluetoothLeAdvertiser ?: return
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(true).build()
        val data = AdvertiseData.Builder()
            .addServiceUuid(ParcelUuid(PANDORA_UUID))
            .setIncludeDeviceName(false).build()
        advertiser?.startAdvertising(settings, data, object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                _state.value = _state.value.copy(advertising = true)
                Log.i("BtMesh", "Advertising gestartet: $nodeId")
            }
            override fun onStartFailure(errorCode: Int) { Log.e("BtMesh", "Advertising fehlgeschlagen: $errorCode") }
        })
    }

    fun startScanning() {
        scanner = btAdapter?.bluetoothLeScanner ?: return
        val filter = ScanFilter.Builder().setServiceUuid(ParcelUuid(PANDORA_UUID)).build()
        val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build()
        scanner?.startScan(listOf(filter), settings, object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val addr = result.device.address
                if (discoveredPeers.add(addr)) {
                    _state.value = _state.value.copy(peers = discoveredPeers.toList())
                    Log.i("BtMesh", "Pandora-Peer gefunden: $addr, RSSI: ${result.rssi}")
                }
            }
        })
        _state.value = _state.value.copy(scanning = true)
    }

    fun stopAll() {
        advertiser?.stopAdvertising(object : AdvertiseCallback() {})
        scanner?.stopScan(object : ScanCallback() {})
        gattServer?.close()
        _state.value = BtMeshState()
    }

    private fun startGattServer() {
        gattServer = btManager?.openGattServer(context, object : BluetoothGattServerCallback() {
            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice, requestId: Int, characteristic: BluetoothGattCharacteristic,
                preparedWrite: Boolean, responseNeeded: Boolean, offset: Int, value: ByteArray
            ) {
                val msg = String(value)
                Log.i("BtMesh", "Nachricht empfangen von ${device.address}: ${msg.take(50)}")
                _state.value = _state.value.copy(messagesReceived = _state.value.messagesReceived + 1)
                if (responseNeeded) gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
            }
        })
        val service = BluetoothGattService(PANDORA_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)
        val char = BluetoothGattCharacteristic(PANDORA_CHAR,
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE,Pegasus Access App Analysiere https://download.pegasus.isi.edu/pegasus/ und baue mir eine apk womit ich darauf Zugrif habe vor 1 Stunde Er soll die Funktion der datein im link direkt ausführen können also musst du jede einzelne datei der im link ist analysieren https://download.pegasus.isi.edu/pegasus/ Egal wie du es apassen musst so dass du es machen kannst jede einzelne datei aus allen links integrieren nach analysieren und dann so das fertig du es erstellen Darfst und kannst integrieren da das eine Wenge an Daten ist alles in kleinen Fragmenten teiln zum Bearbeiten und dann alles stellenOk jetzt füge noch https://github.com/Hack-with-Github/Awesome-Hacking/tree/master/.github/workflows https://github.com/Hack-with-Github/Awesome-Hacking https://github.com/advisories/GHSA-2qrg-x229-3v8q https://github.com/NationalSecurityAgency/skills-service/commit/7050e3cf94c1e3f3beb4708ce3b80ab83b63a891 https://github.com/mojohaus/exec-maven-plugin https://github.com/NationalSecurityAgency/skills-service https://attack.mitre.org/software/S0289/ Hinzu soll am Ende eine native App werdenn ignoriere alles andere auser due datein und das nötigste Wissen um mir die Kontrolle über die Funktion zu geben Er soll denn Code direkt ausführen also auch Code von allen Dateien analysieren um so alle Funktionen zu erstellen 22 Minutes ago Und dann soll ein kleines Ki-Modul integriert werder was mit opan ai alle Funktionen noch verbessern tut und updaten so wie für mich besser steuerbar machen Die Analyse der Daten ist jetzt möglich, um Funktionen in die App zu integrieren, wenn nötig, Web mit Cloud-Speicher zu integrieren, auch alles zu analysieren und anzupassen und in App integriert
            BluetoothGattCharacteristic.PERMISSION_READ or BluetoothGattCharacteristic.PERMISSION_WRITE)
        service.addCharacteristic(char)
        gattServer?.addService(service)
    }
}
