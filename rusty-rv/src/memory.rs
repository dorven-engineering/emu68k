use crate::defs::{AddrRange, Address};
use bytes::{Bytes, BytesMut};

pub enum MemoryAccessResult {
	SuccessfulRead(Bytes),
	SuccessfulWrite(BytesMut),
	InvalidAddress,
	DeviceFailure,
	WriteFailAt(Vec<AddrRange>), // Writes and reads can fail at multiple positions for multiple ranges of addresses.
	ReadFailAt(Vec<AddrRange>),
}

pub trait MemoryDevice {
	fn device_size(&self) -> usize;

	fn read_at(&self, addr: Address) -> MemoryAccessResult;

	fn write_at(&mut self, addr: Address) -> MemoryAccessResult;
}