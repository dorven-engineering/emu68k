use bytes::{Bytes, BytesMut};

trait MemoryDevice {
	fn read_at(&self, addr: u64) -> Bytes;
	fn write_at(&self, addr: u64) -> BytesMut;
}