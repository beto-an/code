import torch

print(f"Is CUDA supported by this system? {torch.cuda.is_available()}")
print(f"CUDA version: {torch.version.cuda}")
# Storing ID of current CUDA device
cuda_id = torch.cuda.current_device()
print(f"ID of current CUDA device:{torch.cuda.current_device()}")
print(f"Name of current CUDA device:{torch.cuda.get_device_name(cuda_id)}")
print(f"Memory Allocated: {torch.cuda.memory_allocated(cuda_id)}")
print(f"Memory Reserved: {torch.cuda.memory_reserved(cuda_id)}")
print(f"Number of available GPUs: {torch.cuda.device_count()}")