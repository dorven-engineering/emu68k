use bytes::{Bytes, BytesMut};
use crate::defs::Address;

pub enum MemoryAccessResult {
	SuccessfulRead(Bytes),
	SuccessfulWrite(BytesMut),
	InvalidAddress,
	DeviceFailure,
}

pub trait MemoryDevice {

	fn read_at(&self, addr: Address) -> Bytes;
	fn write_at(&self, addr: Address) -> BytesMut;
}